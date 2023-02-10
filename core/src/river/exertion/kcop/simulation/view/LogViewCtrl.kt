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
import ktx.actors.*
import river.exertion.kcop.system.view.ViewType
import kotlin.reflect.jvm.javaMethod


class LogViewCtrl(screenWidth: Float = 50f, screenHeight: Float = 50f) : ViewCtrl(ViewType.LOG, screenWidth, screenHeight) {

    var currentLog : MutableList<String>? = null

    var immersionTimeStr = "00:00:00"
    var localTimeStr = "00:00:00"

    var vScrollTexture : Texture? = null
    var vScrollKnobTexture : Texture? = null

    private lateinit var scrollPane : ScrollPane

    fun isLog() = !currentLog.isNullOrEmpty()

    fun addLog(logEntry : String) {
        if (currentLog == null) currentLog = mutableListOf()
        currentLog!!.add(logEntry)
    }

    fun updateImmersionTime(newImmersionTimeStr : String) {
        immersionTimeStr = newImmersionTimeStr
    }

    fun updateLocalTime(newLocalTimeStr : String) {
        localTimeStr = newLocalTimeStr
    }

    fun textTimeReadout(bitmapFont: BitmapFont, batch: Batch) : Stack {

        val innerTable = Table().padLeft(ViewType.padWidth(width)).padRight(ViewType.padWidth(width)).padTop(ViewType.padHeight(height)).padBottom(ViewType.padHeight(height))

        innerTable.add(Label(immersionTimeStr, Label.LabelStyle(bitmapFont, backgroundColor.label().color()))).padRight(this.tableWidth() / 6)
        innerTable.add(Label(localTimeStr, Label.LabelStyle(bitmapFont, backgroundColor.label().color()))).padLeft(this.tableWidth() / 6)

        innerTable.debug()

        val returnStack = Stack().apply {
            this.add(backgroundColorImg(batch))
            this.add(innerTable)
        }

        return returnStack
    }

    fun rebuildTextTimeReadout() {
        if (this.bitmapFont == null) throw Exception("${::recreate.javaMethod?.name}: bitmapFont needs to be set")
        if (this.batch == null) throw Exception("${::recreate.javaMethod?.name}: batch needs to be set")

        this.clearChildren()
        this.add(textTimeReadout(this.bitmapFont!!, this.batch!!)).height(30f)
        this.row()
        this.add(scrollPane)
    }

    fun textScrollPane(bitmapFont: BitmapFont, batch : Batch) : ScrollPane {

        val innerTable = Table().padLeft(ViewType.padWidth(width)).padRight(ViewType.padWidth(width)).padTop(ViewType.padHeight(height)).padBottom(ViewType.padHeight(height))

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
        this.add(textTimeReadout(bitmapFont, batch) ).height(30f)
        this.row()
        this.add(textScrollPane(bitmapFont, batch) )
    }
}