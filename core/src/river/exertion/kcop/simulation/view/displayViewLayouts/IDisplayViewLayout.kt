package river.exertion.kcop.simulation.view.displayViewLayouts

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import river.exertion.kcop.assets.FontSize
import river.exertion.kcop.assets.KcopSkin
import river.exertion.kcop.simulation.view.DisplayViewPaneType
import river.exertion.kcop.simulation.view.ViewType
import river.exertion.kcop.system.colorPalette.ColorPalette
import river.exertion.kcop.system.view.SdcHandler

interface IDisplayViewLayout {

    val tag : String //used for assignment in narrative asset

    var screenWidth : Float
    var screenHeight : Float

    var currentLayoutMode : Boolean
    var currentText : String
    var currentFontSize : FontSize

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

    fun definePanes() : DisplayViewPanes
    fun buildPaneTable() : Table
    fun imagePanes() : List<Int>
    fun textPanes() : List<Int>

    fun paneColorTexture(displayViewPaneIdx : Int, displayViewPaneType : DisplayViewPaneType, overrideColor : ColorPalette?) : TextureRegion {
        return sdcHandler.get("pane_${displayViewPaneIdx}", overrideColor ?: KcopSkin.BackgroundColor).textureRegion().apply {
            this.setRegion(0, 0, displayViewPaneType.width(screenWidth).toInt() - 1, displayViewPaneType.height(screenHeight).toInt() - 1)
        }
    }

    fun paneBATexture(displayViewPaneIdx : Int, displayViewPaneType : DisplayViewPaneType) : TextureRegion {
        return sdcHandler.getBlackAlpha("bapane_${displayViewPaneIdx}", paneTextureMaskAlpha[displayViewPaneIdx] ?: 1f).textureRegion().apply {
            this.setRegion(0, 0, displayViewPaneType.width(screenWidth).toInt() - 1, displayViewPaneType.height(screenHeight).toInt() - 1)
        }
    }

    fun paneText(panes : DisplayViewPanes) : DisplayTextPanes {

        val returnMap = DisplayTextPanes()

        if (currentText.isEmpty() || textPanes().isEmpty()) return returnMap

        var textParsed = false

        var currentTextRemaining = currentText
        var currentDVPIdx = 0

        val textDVPs = textPanes().associateWith { panes.data[it] }
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

                        if (returnMap.data[textDVPs.keys.toList()[currentDVPIdx]] == null) {
                            returnMap.data[textDVPs.keys.toList()[currentDVPIdx]] = dvpText.trim() + "\n"
                        } else {
                            returnMap.data[textDVPs.keys.toList()[currentDVPIdx]] += dvpText.trim() + "\n"
                        }

                        dvpText = currentTextRemaining

                    }
                }
            }

            currentDVPIdx += 1
        }

        return returnMap
    }

    fun layoutPane(displayViewPaneIdx : Int, displayViewPaneType : DisplayViewPaneType) : Stack {
        val randomColor = ColorPalette.randomW3cBasic()
        val label = displayViewPaneIdx.toString() + if (textPanes().contains(displayViewPaneIdx)) ":T" else if (imagePanes().contains(displayViewPaneIdx)) ":I" else ""
        val innerTableBg = Table()
        innerTableBg.add(Image(TextureRegionDrawable(paneColorTexture(displayViewPaneIdx, displayViewPaneType, randomColor).texture))).size(
                displayViewPaneType.width(screenWidth) + (paneRefiners[displayViewPaneIdx]?.x ?: 0f),
                displayViewPaneType.height(screenHeight) + (paneRefiners[displayViewPaneIdx]?.y ?: 0f)
        ).grow()
        innerTableBg.debug = true
        val innerTableFg = Table()
        val innerLabel = Label(label, kcopSkin.labelStyle(FontSize.TEXT, randomColor.label().color()))
        innerLabel.setAlignment(Align.center)
        innerTableFg.add(innerLabel).size(
                displayViewPaneType.width(screenWidth) + (paneRefiners[displayViewPaneIdx]?.x ?: 0f),
                displayViewPaneType.height(screenHeight) + (paneRefiners[displayViewPaneIdx]?.y ?: 0f)
        ).grow()
        innerTableFg.debug = true

        return Stack().apply {
            this.add(innerTableBg)
            this.add(innerTableFg)
        }
    }

    fun imagePane(displayViewPaneIdx : Int, displayViewPaneType : DisplayViewPaneType) : Stack {
        val innerTable = Table()
        innerTable.add(Image(TextureRegionDrawable(TextureRegion(paneTextures[displayViewPaneIdx])))).size(
                displayViewPaneType.width(screenWidth) + (paneRefiners[displayViewPaneIdx]?.x ?: 0f),
                displayViewPaneType.height(screenHeight) + (paneRefiners[displayViewPaneIdx]?.y ?: 0f)
        ).grow()

        return Stack().apply {
            this.add(innerTable)
        }
    }

    fun textPane(displayViewPaneIdx : Int, displayViewPaneType : DisplayViewPaneType, paneText : String) : Stack {
        val textLabel = Label(paneText, kcopSkin.labelStyle(currentFontSize, ColorPalette.of("cyan").color()))
                .apply { this.setAlignment(Align.topLeft) }
        textLabel.setAlignment(Align.topLeft)
        val textTable = Table().padLeft(ViewType.padWidth(screenWidth)).padRight(ViewType.padWidth(screenWidth))
                .padBottom(ViewType.padHeight(screenHeight) - (adjacencyTopPadOffset(currentFontSize)[displayViewPaneIdx] ?: 0))
                .padTop(ViewType.padHeight(screenHeight) + (adjacencyTopPadOffset(currentFontSize)[displayViewPaneIdx] ?: 0))
        textTable.top()
        textTable.add(textLabel).size(
                displayViewPaneType.width(screenWidth) - 2 * ViewType.padWidth(screenWidth),
                displayViewPaneType.height(screenHeight) - 2 * ViewType.padHeight(screenHeight)
        ).grow()
        val innerTableFg = Table()
        innerTableFg.top()
        innerTableFg.add(textTable).size(
                displayViewPaneType.width(screenWidth) + (paneRefiners[displayViewPaneIdx]?.x ?: 0f),
                displayViewPaneType.height(screenHeight) + (paneRefiners[displayViewPaneIdx]?.y ?: 0f)
        ).grow()

        return Stack().apply {
            this.add(innerTableFg)
        }
    }

    fun alphaPane(displayViewPaneIdx : Int, displayViewPaneType : DisplayViewPaneType) : Stack {
        val innerTableFg = Table()
        innerTableFg.add(Image(TextureRegionDrawable(paneBATexture(displayViewPaneIdx, displayViewPaneType)))).size(
                displayViewPaneType.width(screenWidth) + (paneRefiners[displayViewPaneIdx]?.x ?: 0f),
                displayViewPaneType.height(screenHeight) + (paneRefiners[displayViewPaneIdx]?.y ?: 0f)
        ).grow()

        return Stack().apply {
            this.add(innerTableFg)
        }
    }

    fun emptyPane(displayViewPaneIdx : Int, displayViewPaneType : DisplayViewPaneType) : Table {
        return Table().apply {
            this.add(Table()).size(
                    displayViewPaneType.width(screenWidth) + (paneRefiners[displayViewPaneIdx]?.x ?: 0f),
                    displayViewPaneType.height(screenHeight) + (paneRefiners[displayViewPaneIdx]?.y ?: 0f)
            )
        }
    }

    fun buildPaneCtrls() : MutableMap<Int, Stack> {

        val paneCtrls : MutableMap<Int, Stack> = mutableMapOf()
        val panes = definePanes()

        val paneText = if (currentText.isNotBlank()) paneText(panes).data else mutableMapOf()

        panes.data.entries.sortedBy { it.key }.forEach { displayViewPane ->

            paneCtrls[displayViewPane.key] =
                Stack().apply {

                    if (currentLayoutMode) { //fill each pane with random color
                        this.add(layoutPane(displayViewPane.key, displayViewPane.value))
                    } else { //draw specific content if present
                        //holds place for pane size
                        this.add(emptyPane(displayViewPane.key, displayViewPane.value))

                        //image present
                        if (paneTextures[displayViewPane.key] != null) {
                            this.add(imagePane(displayViewPane.key, displayViewPane.value))
                        }
                        //text present
                        if (paneText[displayViewPane.key] != null) {
                            this.add(textPane(displayViewPane.key, displayViewPane.value, paneText[displayViewPane.key]!!))
                        }
                        //alpha masking
                        if (paneTextureMaskAlpha[displayViewPane.key] != null) {
                            this.add(alphaPane(displayViewPane.key, displayViewPane.value))
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