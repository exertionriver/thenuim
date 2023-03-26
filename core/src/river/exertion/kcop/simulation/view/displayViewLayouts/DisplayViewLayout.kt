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
import river.exertion.kcop.assets.FontSize
import river.exertion.kcop.simulation.view.DisplayViewPane
import river.exertion.kcop.simulation.view.ViewType
import river.exertion.kcop.system.view.ShapeDrawerConfig
import river.exertion.kcop.system.colorPalette.ColorPalette

interface DisplayViewLayout {

    val tag : String //used for assignment in narrative asset
    fun tag() = tag

    var screenWidth : Float
    var screenHeight : Float

    val maskPixmap : Pixmap
    val sdcMap : MutableMap<Int, ShapeDrawerConfig?>

    val paneTextures : MutableMap<Int, Texture?>
    val paneTextureMaskAlpha : MutableMap<Int, Float?>
    val paneRefiners : MutableMap<Int, Vector2?>
    val paneImageFading : MutableMap<Int, Boolean?>

    //TODO: flag for enabling / disabling text continuation across adjacency

    fun adjacencyPaneRows(currentFontSize: FontSize) : MutableMap<Int, Int?> //key is textPanes Idx
    fun adjacencyTopPadOffset(currentFontSize: FontSize) : MutableMap<Int, Int?> //key is panes Idx; used with extra rows to give the impression of continuous text between panes

//    var subsequentDVPTextOffset : Int

    fun definePanes() : MutableMap<Int, DisplayViewPane>
    fun buildPaneTable(bitmapFont : BitmapFont, batch : Batch, layoutMode : Boolean, currentText : String, currentFontSize: FontSize) : Table
    fun imagePanes() : List<Int>
    fun textPanes() : List<Int>

    fun Pixmap.setAlpha(alpha : Float?) {
        this.setColor(ColorPalette.of("black").color().r, ColorPalette.of("black").color().g, ColorPalette.of("black").color().b, alpha ?: 1f)
        this.fill()
    }

    fun paneColorTexture(batch : Batch, pane : MutableMap.MutableEntry<Int, DisplayViewPane>, overrideColor : ColorPalette?) : TextureRegion {

        val paneColor = overrideColor ?: ColorPalette.of("black")

        if (sdcMap[pane.key] != null) sdcMap[pane.key]!!.dispose()

        sdcMap[pane.key] = ShapeDrawerConfig(batch, paneColor.color())

        return sdcMap[pane.key]!!.textureRegion.apply {this.setRegion(0, 0,
            pane.value.width(screenWidth).toInt() - 1,
            pane.value.height(screenHeight).toInt() - 1)
        }
    }

    fun paneText(bitmapFont : BitmapFont, currentText: String, currentFontSize: FontSize) : MutableMap<Int, String?> {
        bitmapFont.data.setScale(currentFontSize.fontScale())

        val returnMap : MutableMap<Int, String?> = mutableMapOf()

        if (currentText.isEmpty() || textPanes().isEmpty()) return returnMap

        val panes = definePanes()
        var textParsed = false

        var currentTextRemaining = currentText
        var currentDVPIdx = 0

        val textDVPs = textPanes().associateWith { panes[it] }
        var dvpText = currentTextRemaining

        var extraRow : Int

        while (!textParsed) {
            var paneParsed = false

            val dvpPaneHeight = (textDVPs.values.toList()[currentDVPIdx]!!.height(screenHeight) - 2 * ViewType.padHeight(screenHeight))
            var dvpRows = 0
            extraRow = adjacencyPaneRows(currentFontSize)[currentDVPIdx] ?: 0

            while (!paneParsed && !textParsed) {
                var rowParsed = false

                while (!rowParsed) {
                    val dvpRowWidth = (textDVPs.values.toList()[currentDVPIdx]!!.width(screenWidth) - 2 * ViewType.padWidth(screenWidth))

                    var textLabel = Label(dvpText, Label.LabelStyle(bitmapFont, ColorPalette.of("cyan").color()))

                    val dvpPaneModHeight = dvpPaneHeight + extraRow * textLabel.height

                    val textLabelSize = textLabel.width //* textLabel.height

                    if (textLabelSize > dvpRowWidth) {
                        val lastSpaceIdx = dvpText.lastIndexOf(" ")
                        dvpText = dvpText.substring(0, lastSpaceIdx)
                    } else {

                        if (dvpText.contains("\n") ) {
                            dvpText = dvpText.substring(0, dvpText.indexOf("\n"))
                            //recalc text label, removing \n
                            textLabel = Label(dvpText, Label.LabelStyle(bitmapFont, ColorPalette.of("cyan").color()))
                            currentTextRemaining = currentTextRemaining.substring(dvpText.length + 1, currentTextRemaining.length)
                        } else {
                            currentTextRemaining = currentTextRemaining.substring(dvpText.length, currentTextRemaining.length)
                        }

                        rowParsed = true
                        dvpRows++
                        if ((dvpRows * textLabel.height) > dvpPaneModHeight) paneParsed = true

                        if (currentTextRemaining.isEmpty()) {
                            textParsed = true
                        } else { //end of text fields
                            if ( (currentDVPIdx == textDVPs.size - 1) && paneParsed) {
                                dvpText += "..."
                                textParsed = true
                            }
                        }

                        if (returnMap[textDVPs.keys.toList()[currentDVPIdx]] == null) {
                            returnMap[textDVPs.keys.toList()[currentDVPIdx]] = dvpText.trim() + "\n"
                        } else {
                            returnMap[textDVPs.keys.toList()[currentDVPIdx]] += dvpText.trim() + "\n"
                        }

                        dvpText = currentTextRemaining

                    }
                }
            }

            currentDVPIdx += 1
        }

        return returnMap
    }

    fun buildPaneCtrls(bitmapFont: BitmapFont, batch: Batch, layoutMode : Boolean, currentText : String, currentFontSize: FontSize) : MutableMap<Int, Stack> {

        val paneCtrls : MutableMap<Int, Stack> = mutableMapOf()
        val paneText = if (currentText.isNotBlank()) paneText(bitmapFont, currentText, currentFontSize) else mutableMapOf()

        definePanes().entries.sortedBy { it.key }.forEach { displayViewPane ->
            paneCtrls[displayViewPane.key] =
                Stack().apply {
                    if (layoutMode) { //fill each pane with random color
                        val randomColor = ColorPalette.randomW3cBasic()
                        val label = displayViewPane.key.toString() + if (textPanes().contains(displayViewPane.key)) ":T" else if (imagePanes().contains(displayViewPane.key)) ":I" else ""
                        bitmapFont.data.setScale(FontSize.TEXT.fontScale())
                        val innerTableBg = Table()
                        innerTableBg.add(Image(TextureRegionDrawable(paneColorTexture(batch, displayViewPane, randomColor)))).size(
                            displayViewPane.value.width(screenWidth) + (paneRefiners[displayViewPane.key]?.x ?: 0f),
                            displayViewPane.value.height(screenHeight) + (paneRefiners[displayViewPane.key]?.y ?: 0f)
                        ).grow()
                        innerTableBg.debug = layoutMode
                        val innerTableFg = Table()
                        val innerLabel = Label(label, Label.LabelStyle(bitmapFont, randomColor.label().color()))
                        innerLabel.setAlignment(Align.center)
                        innerTableFg.add(innerLabel).size(
                            displayViewPane.value.width(screenWidth) + (paneRefiners[displayViewPane.key]?.x ?: 0f),
                            displayViewPane.value.height(screenHeight) + (paneRefiners[displayViewPane.key]?.y ?: 0f)
                        ).grow()
                        innerTableFg.debug = layoutMode
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
                            innerTable.debug = layoutMode
                            this.add(innerTable)
                            contentRendered = true
                        }
                        if (paneText[displayViewPane.key] != null) { //text present
                            bitmapFont.data.setScale(currentFontSize.fontScale())
                            val textLabel = Label(paneText[displayViewPane.key], Label.LabelStyle(bitmapFont, ColorPalette.of("cyan").color())).apply { this.setAlignment(Align.topLeft) }
                            textLabel.setAlignment(Align.topLeft)
                            textLabel.debug = layoutMode
                            val textTable = Table().padLeft(ViewType.padWidth(screenWidth)).padRight(ViewType.padWidth(screenWidth))
                                .padBottom(ViewType.padHeight(screenHeight) - (adjacencyTopPadOffset(currentFontSize)[displayViewPane.key] ?: 0))
                                .padTop(ViewType.padHeight(screenHeight) + (adjacencyTopPadOffset(currentFontSize)[displayViewPane.key] ?: 0))
                            textTable.top()
                            textTable.debug = layoutMode
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
                            innerTable.debug = layoutMode
                            this.add(innerTable)
                            contentRendered = true
                        }
                        if (paneTextureMaskAlpha[displayViewPane.key] != null) {
                            val innerTable = Table()
                            innerTable.add(Image(TextureRegionDrawable(TextureRegion(Texture(maskPixmap.apply { this.setAlpha(paneTextureMaskAlpha[displayViewPane.key]); this.fill() }))))).size(
                                displayViewPane.value.width(screenWidth) + (paneRefiners[displayViewPane.key]?.x ?: 0f),
                                displayViewPane.value.height(screenHeight) + (paneRefiners[displayViewPane.key]?.y ?: 0f)
                            ).grow()
                            innerTable.debug = layoutMode
                            this.add(innerTable)
                            contentRendered = true
                        }
                        if (!contentRendered) { //background black
                            val innerTable = Table()
                            innerTable.add(Image(TextureRegionDrawable(paneColorTexture(batch, displayViewPane, null)))).size(
                                displayViewPane.value.width(screenWidth) + (paneRefiners[displayViewPane.key]?.x ?: 0f),
                                displayViewPane.value.height(screenHeight) + (paneRefiners[displayViewPane.key]?.y ?: 0f)
                            ).grow()
                            innerTable.debug = layoutMode
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