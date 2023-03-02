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
            pane.value.width(screenWidth).toInt() - 1,
            pane.value.height(screenHeight).toInt() - 1)
        }
    }

    fun paneText(bitmapFont : BitmapFont) : MutableMap<Int, String?> {
        bitmapFont.data.setScale(FreeTypeFontAssets.largeScale)

        val returnMap : MutableMap<Int, String?> = mutableMapOf()

        if (currentText.isEmpty()) return returnMap

        val panes = definePanes()
        var textParsed = false

        var currentTextRemaining = currentText.trim()
        var currentDVPIdx = 0

        val textDVPs = textPanes().associateWith { panes[it] }
        var dvpText = currentTextRemaining

//        val extraRow = 3

        while (!textParsed) {
            var paneParsed = false

            val dvpPaneHeight = (textDVPs.values.toList()[currentDVPIdx]!!.height(screenHeight) - 2 * ViewType.padHeight(screenHeight))
            var dvpRows = 0

            while (!paneParsed && !textParsed) {
                var rowParsed = false

                while (!rowParsed) {
                    val dvpRowWidth = (textDVPs.values.toList()[currentDVPIdx]!!.width(screenWidth) - 2 * ViewType.padWidth(screenWidth))

                    val textLabel = Label(dvpText, Label.LabelStyle(bitmapFont, ColorPalette.of("cyan").color()))

                    //          dvpDimensions += extraRow * textLabel.height * (textDVPs.values.toList()[currentDVPIdx]!!.width(screenWidth) - 2 * ViewType.padWidth(screenWidth)) * smallWhitespaceFactor

                    val textLabelSize = textLabel.width //* textLabel.height

                    if (textLabelSize > dvpRowWidth) {
                        val lastSpaceIdx = dvpText.lastIndexOf(" ")
                        dvpText = dvpText.substring(0, lastSpaceIdx)
                    } else {
                        currentTextRemaining = currentTextRemaining.substring(dvpText.length, currentTextRemaining.length).trim()

                        rowParsed = true
                        dvpRows++
                        if ((dvpRows * textLabel.height) > dvpPaneHeight) paneParsed = true

                        if (currentTextRemaining.isEmpty()) {
                            textParsed = true
                        } else { //end of text fields
                            if ( (currentDVPIdx == textDVPs.size - 1) && paneParsed) {
                                dvpText += "..."
                                textParsed = true
                            }
                        }

                        if (returnMap[textDVPs.keys.toList()[currentDVPIdx]] == null) {
                            returnMap[textDVPs.keys.toList()[currentDVPIdx]] = dvpText + "\n"
                        } else {
                            returnMap[textDVPs.keys.toList()[currentDVPIdx]] += dvpText + "\n"
                        }

                        dvpText = currentTextRemaining

                    }
                }
            }

            currentDVPIdx += 1
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
                        val innerTableBg = Table()
                        innerTableBg.add(Image(TextureRegionDrawable(paneColorTexture(batch, displayViewPane, randomColor)))).size(
                            displayViewPane.value.width(screenWidth) + (paneRefiners[displayViewPane.key]?.x ?: 0f),
                            displayViewPane.value.height(screenHeight) + (paneRefiners[displayViewPane.key]?.y ?: 0f)
                        ).grow()
                        innerTableBg.debug()
                        val innerTableFg = Table()
                        val innerLabel = Label(label, Label.LabelStyle(bitmapFont, randomColor.label().color()))
                        innerLabel.setAlignment(Align.center)
                        innerTableFg.add(innerLabel).size(
                            displayViewPane.value.width(screenWidth) + (paneRefiners[displayViewPane.key]?.x ?: 0f),
                            displayViewPane.value.height(screenHeight) + (paneRefiners[displayViewPane.key]?.y ?: 0f)
                        ).grow()
                        innerTableFg.debug()
                        this.add(innerTableBg)
                        this.add(innerTableFg)
                    } else { //draw specified content or black
                        var contentRendered = false
                        if (paneTextures[displayViewPane.key] != null) { //image present
                            val innerTable = Table()
                            innerTable.add(Image(TextureRegionDrawable(TextureRegion(paneTextures[displayViewPane.key])))).size(
                                displayViewPane.value.width(screenWidth) + (paneRefiners[displayViewPane.key]?.x ?: 0f),
                                displayViewPane.value.height(screenHeight) + (paneRefiners[displayViewPane.key]?.y ?: 0f)
                            ).grow()
                            innerTable.debug()
                            this.add(innerTable)
                            contentRendered = true
                        }
                        if (paneText[displayViewPane.key] != null) { //text present
                            bitmapFont.data.setScale(FreeTypeFontAssets.largeScale)
                            val textLabel = Label(paneText[displayViewPane.key], Label.LabelStyle(bitmapFont, ColorPalette.of("cyan").color())).apply { this.setAlignment(Align.topLeft) }
                            textLabel.setAlignment(Align.topLeft)
                            textLabel.debug()
                            val textTable = Table().padLeft(ViewType.padWidth(screenWidth)).padRight(ViewType.padWidth(screenWidth)).padBottom(
                            ViewType.padHeight(screenHeight)).padTop(ViewType.padHeight(screenHeight))
                            textTable.top()
                            textTable.debug()
                            textTable.add(textLabel).size(
                                displayViewPane.value.width(screenWidth) - 2 * ViewType.padWidth(screenWidth),
                                displayViewPane.value.height(screenHeight) - 2 * ViewType.padHeight(screenHeight)
                            ).grow()
                            val innerTable = Table()
                            innerTable.top()
                            innerTable.add(textTable).size(
                                displayViewPane.value.width(screenWidth) + (paneRefiners[displayViewPane.key]?.x ?: 0f),
                                displayViewPane.value.height(screenHeight) + (paneRefiners[displayViewPane.key]?.y ?: 0f)
                            ).grow()
                            innerTable.debug()
                            this.add(innerTable)
                            contentRendered = true
                        }
                        if (paneTextureMaskAlpha[displayViewPane.key] != null) {
                            val innerTable = Table()
                            innerTable.add(Image(TextureRegionDrawable(TextureRegion(Texture(maskPixmap.apply { this.setAlpha(paneTextureMaskAlpha[displayViewPane.key]); this.fill() }))))).size(
                                displayViewPane.value.width(screenWidth) + (paneRefiners[displayViewPane.key]?.x ?: 0f),
                                displayViewPane.value.height(screenHeight) + (paneRefiners[displayViewPane.key]?.y ?: 0f)
                            ).grow()
                            innerTable.debug()
                            this.add(innerTable)
                            contentRendered = true
                        }
                        if (!contentRendered) { //background black
                            val innerTable = Table()
                            innerTable.add(Image(TextureRegionDrawable(paneColorTexture(batch, displayViewPane, null)))).size(
                                displayViewPane.value.width(screenWidth) + (paneRefiners[displayViewPane.key]?.x ?: 0f),
                                displayViewPane.value.height(screenHeight) + (paneRefiners[displayViewPane.key]?.y ?: 0f)
                            ).grow()
                            innerTable.debug()
                            this.add(innerTable)
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