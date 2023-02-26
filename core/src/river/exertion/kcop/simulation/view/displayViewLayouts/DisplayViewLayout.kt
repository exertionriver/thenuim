package river.exertion.kcop.simulation.view.displayViewLayouts

import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.simulation.view.DisplayViewPane
import river.exertion.kcop.system.ShapeDrawerConfig
import river.exertion.kcop.system.colorPalette.ColorPalette

interface DisplayViewLayout {

    var layoutMode : Boolean
    val maskPixmap : Pixmap
    val sdcMap : MutableMap<Int, ShapeDrawerConfig?>

    val paneTextures : MutableMap<Int, Texture?>
    val paneTextureMaskAlpha : MutableMap<Int, Float?>
    val paneRefiners : MutableMap<Int, Vector2?>

    fun definePanes() : MutableMap<Int, DisplayViewPane>
    fun paneColorTexture(batch : Batch, pane : MutableMap.MutableEntry<Int, DisplayViewPane>, overrideColor : ColorPalette? = null) : TextureRegion
    fun buildPaneCtrls(bitmapFont: BitmapFont, batch: Batch) : MutableMap<Int, Stack>
    fun buildPaneTable(bitmapFont : BitmapFont, batch : Batch) : Table

    fun Pixmap.setAlpha(alpha : Float?) {
        this.setColor(ColorPalette.of("black").color().r, ColorPalette.of("black").color().g, ColorPalette.of("black").color().b, alpha ?: 1f)
        this.fill()
    }

    fun dispose() {
        maskPixmap.dispose()
        sdcMap.values.forEach { it?.dispose() }
        paneTextures.values.forEach { it?.dispose() }
    }
}