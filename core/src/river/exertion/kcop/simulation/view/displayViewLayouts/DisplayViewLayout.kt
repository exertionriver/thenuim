package river.exertion.kcop.simulation.view.displayViewLayouts

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import river.exertion.kcop.assets.FontSize
import river.exertion.kcop.assets.KcopSkin
import river.exertion.kcop.simulation.view.DisplayViewPane
import river.exertion.kcop.simulation.view.FontPackage
import river.exertion.kcop.simulation.view.ViewType
import river.exertion.kcop.system.colorPalette.ColorPalette
import river.exertion.kcop.system.view.SdcHandler

interface DisplayViewLayout {

    val tag : String //used for assignment in narrative asset

    var screenWidth : Float
    var screenHeight : Float

    var sdcHandler : SdcHandler
    var kcopSkin : KcopSkin

    fun skin() = kcopSkin.skin

    val paneTextures : MutableMap<Int, Texture?>
    val paneTextureMaskAlpha : MutableMap<Int, Float?>
    val paneRefiners : MutableMap<Int, Vector2?>

    //TODO: flag for enabling / disabling text continuation across adjacency

    fun adjacencyPaneRows(currentFontSize: FontSize) : MutableMap<Int, Int?> //key is textPanes Idx
    fun adjacencyTopPadOffset(currentFontSize: FontSize) : MutableMap<Int, Int?> //key is panes Idx; used with extra rows to give the impression of continuous text between panes

//    var subsequentDVPTextOffset : Int

    fun definePanes() : MutableMap<Int, DisplayViewPane>
    fun buildPaneTable(layoutMode : Boolean, currentText : String, currentFontSize: FontSize) : Table
    fun imagePanes() : List<Int>
    fun textPanes() : List<Int>

    fun paneColorTexture(pane : MutableMap.MutableEntry<Int, DisplayViewPane>, overrideColor : ColorPalette?) : TextureRegion {
        return sdcHandler.get("pane_${pane.key}", overrideColor ?: ColorPalette.of("black")).textureRegion().apply {
            this.setRegion(0, 0, pane.value.width(screenWidth).toInt() - 1, pane.value.height(screenHeight).toInt() - 1)
        }
    }

    fun paneBATexture(pane : MutableMap.MutableEntry<Int, DisplayViewPane>) : TextureRegion {
        return sdcHandler.getBlackAlpha("bapane_${pane.key}", paneTextureMaskAlpha[pane.key] ?: 1f).textureRegion().apply {
            this.setRegion(0, 0, pane.value.width(screenWidth).toInt() - 1, pane.value.height(screenHeight).toInt() - 1)
        }
    }

    fun paneText(currentText: String, currentFontSize: FontSize) : MutableMap<Int, String?> {

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

                    var textLabel = Label(dvpText, kcopSkin.labelStyle(currentFontSize, ColorPalette.of("cyan").color()))

                    val dvpPaneModHeight = dvpPaneHeight + extraRow * textLabel.height

                    val textLabelSize = textLabel.width //* textLabel.height

                    if (textLabelSize > dvpRowWidth) {
                        val lastSpaceIdx = dvpText.lastIndexOf(" ")
                        dvpText = dvpText.substring(0, lastSpaceIdx)
                    } else {

                        if (dvpText.contains("\n") ) {
                            dvpText = dvpText.substring(0, dvpText.indexOf("\n"))
                            //recalc text label, removing \n
                            textLabel = Label(dvpText, kcopSkin.labelStyle(currentFontSize, ColorPalette.of("cyan").color()))

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

    fun buildPaneCtrls(layoutMode : Boolean, currentText : String, currentFontSize: FontSize) : MutableMap<Int, Stack> {

        val paneCtrls : MutableMap<Int, Stack> = mutableMapOf()
        val paneText = if (currentText.isNotBlank()) paneText(currentText, currentFontSize) else mutableMapOf()

        definePanes().entries.sortedBy { it.key }.forEach { displayViewPane ->

            paneCtrls[displayViewPane.key] =
                Stack().apply {
                    this.add(Table().apply {
                        this.add(Table()).size(
                            displayViewPane.value.width(screenWidth) + (paneRefiners[displayViewPane.key]?.x ?: 0f),
                            displayViewPane.value.height(screenHeight) + (paneRefiners[displayViewPane.key]?.y ?: 0f)
                        )
                    })
                    if (layoutMode) { //fill each pane with random color
                        val randomColor = ColorPalette.randomW3cBasic()
                        val label = displayViewPane.key.toString() + if (textPanes().contains(displayViewPane.key)) ":T" else if (imagePanes().contains(displayViewPane.key)) ":I" else ""
//                        bitmapFont.data.setScale(FontSize.TEXT.fontScale())
                        val innerTableBg = Table()
                        innerTableBg.add(Image(TextureRegionDrawable(paneColorTexture(displayViewPane, randomColor).texture))).size(
                            displayViewPane.value.width(screenWidth) + (paneRefiners[displayViewPane.key]?.x ?: 0f),
                            displayViewPane.value.height(screenHeight) + (paneRefiners[displayViewPane.key]?.y ?: 0f)
                        ).grow()
                        innerTableBg.debug = layoutMode
                        val innerTableFg = Table()
                        val innerLabel = Label(label, kcopSkin.labelStyle(currentFontSize, randomColor.label().color()))
                        innerLabel.setAlignment(Align.center)
                        innerTableFg.add(innerLabel).size(
                            displayViewPane.value.width(screenWidth) + (paneRefiners[displayViewPane.key]?.x ?: 0f),
                            displayViewPane.value.height(screenHeight) + (paneRefiners[displayViewPane.key]?.y ?: 0f)
                        ).grow()
                        innerTableFg.debug = layoutMode
                        this.add(innerTableBg)
                        this.add(innerTableFg)
                    } else { //draw specified content or black
                        //image present
                        if (paneTextures[displayViewPane.key] != null) {
                            val innerTable = Table()
                            innerTable.add(Image(TextureRegionDrawable(TextureRegion(paneTextures[displayViewPane.key])))).size(
                                displayViewPane.value.width(screenWidth) + (paneRefiners[displayViewPane.key]?.x ?: 0f),
                                displayViewPane.value.height(screenHeight) + (paneRefiners[displayViewPane.key]?.y ?: 0f)
                            ).grow()
                            innerTable.debug = layoutMode
                            this.add(innerTable)
                        }
                        //text present
                        if (paneText[displayViewPane.key] != null) {
                            val textLabel = Label(paneText[displayViewPane.key], kcopSkin.labelStyle(currentFontSize, ColorPalette.of("cyan").color()))
                                    .apply { this.setAlignment(Align.topLeft) }
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
                            val innerTableFg = Table()
                            innerTableFg.top()
                            innerTableFg.add(textTable).size(
                                displayViewPane.value.width(screenWidth) + (paneRefiners[displayViewPane.key]?.x ?: 0f),
                                displayViewPane.value.height(screenHeight) + (paneRefiners[displayViewPane.key]?.y ?: 0f)
                            ).grow()
                            innerTableFg.debug = layoutMode
                            this.add(innerTableFg)
                        }
                        //alpha masking
                        if (paneTextureMaskAlpha[displayViewPane.key] != null) {
                            val innerTableFg = Table()
                            innerTableFg.add(Image(TextureRegionDrawable(paneBATexture(displayViewPane)))).size(
                                displayViewPane.value.width(screenWidth) + (paneRefiners[displayViewPane.key]?.x ?: 0f),
                                displayViewPane.value.height(screenHeight) + (paneRefiners[displayViewPane.key]?.y ?: 0f)
                            ).grow()
                            innerTableFg.debug = layoutMode
                            this.add(innerTableFg)
                        }
                    }
                }
            }
        return paneCtrls
    }

    fun dispose() {
        sdcHandler.dispose()
        kcopSkin.dispose()
        paneTextures.values.forEach { it?.dispose() }
    }
}