package river.exertion.kcop.view.layout

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.*
import river.exertion.kcop.asset.view.ColorPalette
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.SdcHandler
import river.exertion.kcop.view.asset.FontSize
import kotlin.math.roundToInt

object StatusView : ViewBase {

    override var viewType = ViewType.STATUS
    override var viewTable = Table()

    val displayStatuses : MutableMap<String, Float> = mutableMapOf()

    private lateinit var scrollPane : ScrollPane

    fun statusColorTexture(overrideColor : ColorPalette? = null) : TextureRegion {
        return SdcHandler.get("statusColor", overrideColor ?: backgroundColor().triad().first).textureRegion().apply {
            this.setRegion(0, 0, ViewType.padWidth(KcopSkin.screenWidth).roundToInt(), ViewType.padHeight(KcopSkin.screenHeight).roundToInt())
        }
    }

    fun textScrollPane() : ScrollPane {

        val innerTable = Table().padLeft(ViewType.padWidth(KcopSkin.screenWidth)).padRight(ViewType.padWidth(KcopSkin.screenWidth)).padTop(
            ViewType.padHeight(
                KcopSkin.screenHeight
            )
        ).padBottom(
            ViewType.padHeight(KcopSkin.screenHeight)
        )

        displayStatuses.forEach {
            val barStack = Stack()

            barStack.add(
                ProgressBar(0f, 1f, .01f, false, KcopSkin.skin).apply { this.value = it.value } )
            barStack.add(
                Label(it.key, KcopSkin.labelStyle(FontSize.TEXT, backgroundColor().triad().first.incr(2)))
            )

            innerTable.add(barStack)
            innerTable.row()
        }

        innerTable.top()
//        innerTable.debug()

            val scrollPane = ScrollPane(innerTable, KcopSkin.skin).apply {
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

    override fun buildCtrl() {
        viewTable.add(Stack().apply {
            this.add(backgroundColorImg())
            this.add(textScrollPane())
        } ).size(this.tableWidth(), this.tableHeight())

        viewTable.clip()
    }

    fun addOrUpdateStatus(key : String, value : Float) {
        displayStatuses[key] = value
    }

    fun clearStatuses() {
        displayStatuses.clear()
    }

    fun removeStatus(key : String) {
        displayStatuses.remove(key)
    }

    fun showCompletionStatus() {}

    fun hideCompletionStatus() {}
}