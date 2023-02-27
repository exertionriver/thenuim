package river.exertion.kcop.simulation.view.displayViewLayouts

import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import river.exertion.kcop.simulation.view.DisplayViewPane
import river.exertion.kcop.system.ShapeDrawerConfig
import river.exertion.kcop.system.colorPalette.ColorPalette
import kotlin.math.roundToInt

interface DisplayViewLayout {

    var screenWidth : Float
    var screenHeight : Float

    var layoutMode : Boolean
    val maskPixmap : Pixmap
    val sdcMap : MutableMap<Int, ShapeDrawerConfig?>

    val paneTextures : MutableMap<Int, Texture?>
    val paneTextureMaskAlpha : MutableMap<Int, Float?>
    val paneRefiners : MutableMap<Int, Vector2?>
    val paneImageFading : MutableMap<Int, Boolean?>

    fun definePanes() : MutableMap<Int, DisplayViewPane>
    fun buildPaneTable(bitmapFont : BitmapFont, batch : Batch) : Table

    fun Pixmap.setAlpha(alpha : Float?) {
        this.setColor(ColorPalette.of("black").color().r, ColorPalette.of("black").color().g, ColorPalette.of("black").color().b, alpha ?: 1f)
        this.fill()
    }

    //used for panes large
    fun paneColorTexture(batch : Batch, pane : MutableMap.MutableEntry<Int, DisplayViewPane>, overrideColor : ColorPalette?) : TextureRegion {

        val paneColor = overrideColor ?: ColorPalette.of("black")

        if (sdcMap[pane.key] != null) sdcMap[pane.key]!!.dispose()

        sdcMap[pane.key] = ShapeDrawerConfig(batch, paneColor.color())

        return sdcMap[pane.key]!!.textureRegion.apply {this.setRegion(0, 0,
            pane.value.width(screenWidth).roundToInt() + (paneRefiners[pane.key]?.x ?: 0f).toInt(),
            pane.value.height(screenHeight).roundToInt() + (paneRefiners[pane.key]?.y ?: 0f).toInt())
        }
    }

    fun buildPaneCtrls(bitmapFont: BitmapFont, batch: Batch) : MutableMap<Int, Stack> {

        val paneCtrls : MutableMap<Int, Stack> = mutableMapOf()

        definePanes().entries.sortedBy { it.key }.forEach { displayViewPane ->
            paneCtrls[displayViewPane.key] =
                Stack().apply {
                    if (layoutMode) { //random color
                        val randomColor = ColorPalette.randomW3cBasic()
                        this.add(Table().apply { this.background = TextureRegionDrawable(paneColorTexture(batch, displayViewPane, randomColor)) })
                        this.add(Table().apply { this.add(Label(displayViewPane.key.toString(), Label.LabelStyle(bitmapFont, randomColor.label().color()))) }.center())
                    } else if (paneTextures[displayViewPane.key] != null) { //image present
                        this.add(Table().apply { this.add(Image(TextureRegionDrawable(TextureRegion(paneTextures[displayViewPane.key])))).size(
                            displayViewPane.value.width(screenWidth),
                            displayViewPane.value.height(screenHeight))
                        })
                        if (paneTextureMaskAlpha[displayViewPane.key] != null) {
                            this.add(Table().apply { this.add(Image(TextureRegionDrawable(TextureRegion(Texture(maskPixmap.apply { this.setAlpha(paneTextureMaskAlpha[displayViewPane.key]); this.fill() }))))).size(
                                displayViewPane.value.width(screenWidth) + (paneRefiners[displayViewPane.key]?.x ?: 0f).toInt(),
                                displayViewPane.value.height(screenHeight) + (paneRefiners[displayViewPane.key]?.y ?: 0f).toInt(),)})
                        }
                    } else { //background black
                        this.add(Table().apply { this.background = TextureRegionDrawable(paneColorTexture(batch, displayViewPane, null)) })
                    }
                }
        }

        return paneCtrls
    }

    fun dispose() {
        maskPixmap.dispose()
        sdcMap.values.forEach { it?.dispose() }
        paneTextures.values.forEach { it?.dispose() }
    }
}