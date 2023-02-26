package river.exertion.kcop.simulation.view

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import kotlin.reflect.jvm.javaMethod


class LogViewCtrl(screenWidth: Float = 50f, screenHeight: Float = 50f) : ViewCtrl(ViewType.LOG, screenWidth, screenHeight) {

    var currentLog : MutableList<String>? = null

    var instImmersionTimeStr = "00:00:00"
    var cumlImmersionTimeStr = "00:00:00"
    var localTimeStr = "00:00:00"

    var vScrollTexture : Texture? = null
    var vScrollKnobTexture : Texture? = null

    private lateinit var scrollPane : ScrollPane

    fun isLog() = !currentLog.isNullOrEmpty()

    fun addLog(logEntry : String) {
        if (currentLog == null) currentLog = mutableListOf()
        currentLog!!.add(logEntry)
    }

    fun updateInstImmersionTime(newImmersionTimeStr : String) {
        instImmersionTimeStr = newImmersionTimeStr
    }

    fun updateCumlImmersionTime(newImmersionTimeStr : String) {
        cumlImmersionTimeStr = newImmersionTimeStr
    }

    fun updateLocalTime(newLocalTimeStr : String) {
        localTimeStr = newLocalTimeStr
    }

    fun textTimeReadout(bitmapFont: BitmapFont) : Table {

        val innerTable = Table().padLeft(ViewType.padWidth(width)).padRight(ViewType.padWidth(width)).padTop(ViewType.padHeight(height)).padBottom(
            ViewType.padHeight(height))

        innerTable.add(Label(instImmersionTimeStr, Label.LabelStyle(bitmapFont, backgroundColor.label().color()))).padRight(this.tableWidth() / 9)
        innerTable.add(Label(cumlImmersionTimeStr, Label.LabelStyle(bitmapFont, backgroundColor.label().color()))).padLeft(this.tableWidth() / 9).padRight(this.tableWidth() / 9)
        innerTable.add(Label(localTimeStr, Label.LabelStyle(bitmapFont, backgroundColor.label().color()))).padLeft(this.tableWidth() / 9)

        innerTable.debug()

        return innerTable
    }

    fun rebuildTextTimeReadout() {
        if (!isInitialized) throw Exception("${::rebuildTextTimeReadout.javaMethod?.name}: view needs to be initialized with " + ::initCreate.javaMethod?.name)

        this.clearChildren()

        this.add(Stack().apply {
            this.add(backgroundColorImg(this@LogViewCtrl.batch!!))
            this.add(Table().apply {
                this.add(textTimeReadout(this@LogViewCtrl.bitmapFont!!))
                this.row()
                this.add(scrollPane)
            })
        })
    }

    fun textScrollPane(bitmapFont: BitmapFont, batch : Batch) : ScrollPane {

        val innerTable = Table().padLeft(ViewType.padWidth(width)).padRight(ViewType.padWidth(width)).padTop(ViewType.padHeight(height)).padBottom(
            ViewType.padHeight(height))

        if (isLog()) {
            (currentLog!!.size - 1 downTo 0).forEach { revEntryIdx ->
                val logLabel = Label(currentLog!![revEntryIdx], Label.LabelStyle(bitmapFont, backgroundColor.label().color()))
                logLabel.wrap = true
                innerTable.add(logLabel).growX()
                innerTable.row()
            }
        }

        innerTable.top()
        innerTable.debug()

        val scrollNine = NinePatch(TextureRegion(vScrollKnobTexture, 20, 20, 20, 20))
        val scrollPaneStyle = ScrollPane.ScrollPaneStyle(TextureRegionDrawable(backgroundColorTexture(batch)), null, null, null, NinePatchDrawable(scrollNine))

        val scrollPane = ScrollPane(innerTable, scrollPaneStyle).apply {
            // https://github.com/raeleus/skin-composer/wiki/ScrollPane
            this.fadeScrollBars = false
            this.setFlickScroll(false)
            this.validate()
            //https://gamedev.stackexchange.com/questions/96096/libgdx-scrollpane-wont-scroll-to-bottom-after-adding-children-to-contained-tab
            this.layout()
        }

        this.scrollPane = scrollPane

        return scrollPane
    }

    override fun build(bitmapFont: BitmapFont, batch: Batch) {

        this.add(Stack().apply {
            this.add(backgroundColorImg(batch))
            this.add(Table().apply {
                this.add(textTimeReadout(bitmapFont))
                this.row()
                this.add(textScrollPane(bitmapFont, batch))
            })
        })
    }
}