package river.exertion.kcop.simulation.view

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import ktx.actors.*
import river.exertion.kcop.system.view.ViewType


class LogViewCtrl(screenWidth: Float = 50f, screenHeight: Float = 50f) : ViewCtrl(ViewType.LOG, screenWidth, screenHeight) {

    var currentLog : MutableList<String>? = null

    var immersionTimeStr = "00:00:00"
    var localTimeStr = "00:00:00"

    var vScrollTexture : Texture? = null
    var vScrollKnobTexture : Texture? = null

    fun isLog() = !currentLog.isNullOrEmpty()

    fun saveScroll() {
        val asdf = (this.children[1] as ScrollPane).scrollY
    }

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

    fun textTimeReadout(bitmapFont: BitmapFont) : Table {

        val innerTable = Table().padLeft(ViewType.padWidth(width)).padRight(ViewType.padWidth(width)).padTop(ViewType.padHeight(height)).padBottom(ViewType.padHeight(height))

        innerTable.add(Label(immersionTimeStr, Label.LabelStyle(bitmapFont, backgroundColor.label().color()))).padRight(this.tableWidth() / 6)
        innerTable.add(Label(localTimeStr, Label.LabelStyle(bitmapFont, backgroundColor.label().color()))).padLeft(this.tableWidth() / 6)

        innerTable.top()
        innerTable.debug()

        return innerTable
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

        return scrollPane
    }

    override fun build(bitmapFont: BitmapFont, batch: Batch) {
        this.add(textTimeReadout(bitmapFont) )
        this.row()
        this.add(textScrollPane(bitmapFont, batch) )
    }
}