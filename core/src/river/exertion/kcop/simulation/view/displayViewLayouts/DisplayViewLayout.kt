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
import com.badlogic.gdx.utils.Align
import river.exertion.kcop.assets.FreeTypeFontAssets
import river.exertion.kcop.simulation.view.DisplayViewPane
import river.exertion.kcop.simulation.view.ViewType
import river.exertion.kcop.system.ShapeDrawerConfig
import river.exertion.kcop.system.colorPalette.ColorPalette
import kotlin.math.roundToInt

interface DisplayViewLayout {

    val tag : String //used for assignment in narrative asset

    var screenWidth : Float
    var screenHeight : Float

    var layoutMode : Boolean
    val maskPixmap : Pixmap
    val sdcMap : MutableMap<Int, ShapeDrawerConfig?>

    var currentText : String

    val paneTextures : MutableMap<Int, Texture?>
    val paneTextureMaskAlpha : MutableMap<Int, Float?>
    val paneRefiners : MutableMap<Int, Vector2?>
    val paneImageFading : MutableMap<Int, Boolean?>

    fun definePanes() : MutableMap<Int, DisplayViewPane>
    fun buildPaneTable(bitmapFont : BitmapFont, batch : Batch) : Table
    fun imagePanes() : List<Int>
    fun textPanes() : List<Int>

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

    fun paneText(bitmapFont : BitmapFont) : MutableMap<Int, String?> {
        bitmapFont.data.setScale(FreeTypeFontAssets.smallScale)

        val returnMap : MutableMap<Int, String?> = mutableMapOf()

        if (currentText.isEmpty()) return returnMap

        val panes = definePanes()
        var textParsed = false

        var currentTextRemaining = currentText.trim()
        var currentDVPIdx = 0

        val textDVPs = textPanes().associateWith { panes[it] }
        var dvpText = currentTextRemaining

        val smallWhitespaceFactor = 1.1
        val mediumWhitespaceFactor = 1.04
        val largeWhitespaceFactor = 1.04

        while (!textParsed) {
            val dvpDimensions = (textDVPs.values.toList()[currentDVPIdx]!!.width(screenWidth) - 2 * ViewType.padWidth(screenWidth)) *
                    (textDVPs.values.toList()[currentDVPIdx]!!.height(screenHeight) - 2 * ViewType.padHeight(screenHeight)) * smallWhitespaceFactor
            val textLabel = Label(dvpText, Label.LabelStyle(bitmapFont, ColorPalette.of("cyan").color()))

            val textLabelSize = textLabel.width * textLabel.height

            if (textLabelSize > dvpDimensions) {
                val lastSpaceIdx = dvpText.lastIndexOf(" ")
                dvpText = dvpText.substring(0, lastSpaceIdx)
            } else {
                currentTextRemaining = currentTextRemaining.substring(dvpText.length, currentTextRemaining.length).trim()

                if (currentTextRemaining.isEmpty()) {
                    textParsed = true
                } else { //end of text fields
                    if (currentDVPIdx == textDVPs.size - 1) {
                        dvpText += "..."
                        textParsed = true
                    }
                }

                returnMap[textDVPs.keys.toList()[currentDVPIdx]] = dvpText

                dvpText = currentTextRemaining

                currentDVPIdx += 1
            }

        }

        return returnMap
    }

    fun buildPaneCtrls(bitmapFont: BitmapFont, batch: Batch) : MutableMap<Int, Stack> {

        val paneCtrls : MutableMap<Int, Stack> = mutableMapOf()
        val paneText = paneText(bitmapFont)

        definePanes().entries.sortedBy { it.key }.forEach { displayViewPane ->
            paneCtrls[displayViewPane.key] =
                Stack().apply {
                    if (layoutMode) { //fill each pane with random color
                        val randomColor = ColorPalette.randomW3cBasic()
                        val label = displayViewPane.key.toString() + if (textPanes().contains(displayViewPane.key)) ":T" else if (imagePanes().contains(displayViewPane.key)) ":I" else ""
                        bitmapFont.data.setScale(FreeTypeFontAssets.textScale)
                        this.add(Table().apply { this.background = TextureRegionDrawable(paneColorTexture(batch, displayViewPane, randomColor)) })
                        this.add(Table().apply { this.add(Label(label, Label.LabelStyle(bitmapFont, randomColor.label().color()))) }.center())
                    } else { //draw specified content or black
                        var contentRendered = false
                        if (paneTextures[displayViewPane.key] != null) { //image present
                            this.add(Table().apply { this.debug(); this.add(Image(TextureRegionDrawable(TextureRegion(paneTextures[displayViewPane.key])))).size(
                                displayViewPane.value.width(screenWidth),
                                displayViewPane.value.height(screenHeight))
                            })
                            contentRendered = true
                        }
                        if (paneText[displayViewPane.key] != null) { //text present
                            bitmapFont.data.setScale(FreeTypeFontAssets.smallScale)
                            val textLabel = Label(paneText[displayViewPane.key], Label.LabelStyle(bitmapFont, ColorPalette.of("cyan").color())).apply { this.setAlignment(Align.topLeft); this.wrap = true }

                            textLabel.debug()
                            val textTable = Table().padLeft(ViewType.padWidth(screenWidth)).padRight(ViewType.padWidth(screenWidth)).padTop(ViewType.padHeight(screenHeight)).padBottom(
                            ViewType.padHeight(screenHeight))
                            textTable.top()
                            textTable.debug()
                            textTable.add(textLabel)
                            .size(
                                displayViewPane.value.width(screenWidth) - 2 * ViewType.padWidth(screenWidth),
                                displayViewPane.value.height(screenHeight) - 2 * ViewType.padHeight(screenHeight)
                            )
                            this.add(textTable)
                            contentRendered = true
                            this.debug()
                        }
                        if (paneTextureMaskAlpha[displayViewPane.key] != null) {
                            this.add(Table().apply { this.add(Image(TextureRegionDrawable(TextureRegion(Texture(maskPixmap.apply { this.setAlpha(paneTextureMaskAlpha[displayViewPane.key]); this.fill() }))))).size(
                                displayViewPane.value.width(screenWidth) + (paneRefiners[displayViewPane.key]?.x ?: 0f).toInt(),
                                displayViewPane.value.height(screenHeight) + (paneRefiners[displayViewPane.key]?.y ?: 0f).toInt(),)})
                            contentRendered = true
                        }
                        if (!contentRendered) { //background black
                            this.add(Table().apply { this.background = TextureRegionDrawable(paneColorTexture(batch, displayViewPane, null)) })
                        }
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