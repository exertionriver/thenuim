package river.exertion.kcop.simulation.view.ctrl

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import river.exertion.kcop.simulation.view.ViewType
import river.exertion.kcop.system.ShapeDrawerConfig
import river.exertion.kcop.system.colorPalette.ColorPalette
import river.exertion.kcop.system.profile.Status
import kotlin.math.roundToInt


class StatusViewCtrl(screenWidth: Float = 50f, screenHeight: Float = 50f) : ViewCtrl(ViewType.STATUS, screenWidth, screenHeight) {

    val statuses : MutableList<Status> = mutableListOf()

    var vScrollKnobTexture : Texture? = null

    private lateinit var scrollPane : ScrollPane

    fun statusColorTexture(batch : Batch, overrideColor : ColorPalette? = null) : TextureRegion {

        if (overrideColor == null)
            this.sdc = ShapeDrawerConfig(batch, backgroundColor.color())
        else
            this.sdc = ShapeDrawerConfig(batch, overrideColor.color())

        return sdc!!.textureRegion.apply {this.setRegion(0, 0, ViewType.padWidth(screenWidth)
            .roundToInt(), ViewType.padHeight(screenHeight).roundToInt()) }
    }

    fun textScrollPane(bitmapFont: BitmapFont, batch : Batch) : ScrollPane {

        val innerTable = Table().padLeft(ViewType.padWidth(width)).padRight(ViewType.padWidth(width)).padTop(
            ViewType.padHeight(
                height
            )
        ).padBottom(
            ViewType.padHeight(height)
        )

        statuses.forEach {
            val barStack = Stack()

            barStack.add(
                ProgressBar(0f, 1f, .01f, false, ProgressBar.ProgressBarStyle(
                    NinePatchDrawable(NinePatch(statusColorTexture(batch))),
                    null
                ).apply { this.knobBefore = NinePatchDrawable(NinePatch(statusColorTexture(batch, backgroundColor.triad().first)))}
            ).apply { this.value = it.value }
            )
            barStack.add(
                Label(it.key, Label.LabelStyle(bitmapFont, backgroundColor.label().incr(2).color()))
            )

            innerTable.add(barStack)
            innerTable.row()
        }

        innerTable.top()
//        innerTable.debug()

        val scrollNine = NinePatch(statusColorTexture(batch, backgroundColor.triad().second.incr(2)))
        val scrollPaneStyle = ScrollPane.ScrollPaneStyle(TextureRegionDrawable(statusColorTexture(batch)), null, null, null, NinePatchDrawable(scrollNine))

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
        this.add(textScrollPane(bitmapFont, batch)).size(this.tableWidth(), this.tableHeight())
        this.clip()
    }
}