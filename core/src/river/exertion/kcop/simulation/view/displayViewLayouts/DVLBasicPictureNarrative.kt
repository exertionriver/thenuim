package river.exertion.kcop.simulation.view.displayViewLayouts

import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import river.exertion.kcop.assets.FreeTypeFontAssets
import river.exertion.kcop.simulation.view.DisplayViewPane
import river.exertion.kcop.simulation.view.ViewType
import river.exertion.kcop.system.ShapeDrawerConfig
import river.exertion.kcop.system.colorPalette.ColorPalette

class DVLBasicPictureNarrative(override var screenWidth: Float, override var screenHeight: Float) : DisplayViewLayout {

    override val tag = "basicPictureNarrative"

    override var layoutMode = true
    override val maskPixmap = Pixmap(16, 16, Pixmap.Format.RGBA8888)

    override var currentText = ""

    override val sdcMap : MutableMap<Int, ShapeDrawerConfig?> = mutableMapOf()
    override val paneTextures : MutableMap<Int, Texture?> = mutableMapOf()
    override val paneTextureMaskAlpha : MutableMap<Int, Float?> = mutableMapOf()
    override val paneImageFading : MutableMap<Int, Boolean?> = mutableMapOf()

    override val paneRefiners : MutableMap<Int, Vector2?> = mutableMapOf(
    )

    override fun definePanes() : MutableMap<Int, DisplayViewPane> {
        val panes : MutableMap<Int, DisplayViewPane> = mutableMapOf()

        //picture
        panes[0] = DisplayViewPane.LARGE_BY_LARGE

        //text blocks
        panes[1] = DisplayViewPane.TITLE_BY_LARGE
        panes[2] = DisplayViewPane.FULL_BY_TITLE

        return panes
    }

    override fun imagePanes() : List<Int> = listOf(0)
    override fun textPanes() : List<Int> = listOf(1, 2)

    override fun buildPaneTable(bitmapFont : BitmapFont, batch : Batch) : Table {

        val panes = buildPaneCtrls(bitmapFont, batch)

        val innerTable = Table()

        val tlRow0 = Table()
        tlRow0.add(panes[0]).fill()
        tlRow0.add(panes[1]).fill()

        val tlFull = Table()
        tlFull.add(tlRow0).fill()
        tlFull.row()
        tlFull.add(panes[2]).fill()

        innerTable.add(tlFull)
        innerTable.validate()
        innerTable.layout()
        return innerTable
    }
}
