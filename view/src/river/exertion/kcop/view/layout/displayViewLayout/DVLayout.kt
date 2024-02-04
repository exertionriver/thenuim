package river.exertion.kcop.view.layout.displayViewLayout

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import river.exertion.kcop.asset.view.ColorPalette
import river.exertion.kcop.base.Id
import river.exertion.kcop.base.KcopBase
import river.exertion.kcop.view.KcopFont
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.layout.ViewType

@Serializable
data class DVLayout(
    var id: String = Id.randomId(),
    val name : String,
    val layout : MutableList<DVTable> = mutableListOf(),
    val textAdjacencyRows: MutableList<DVLTextAdjacencyRow> = mutableListOf(),
    val textAdjacencyTopPads: MutableList<DVLTextAdjacencyTopPad> = mutableListOf()
) {

    @Transient
    var layoutPanes : MutableList<DVPane> = mutableListOf()

    fun layoutPanes() = if (layoutPanes.isNotEmpty() ) layoutPanes else { layout.forEach { panesOf(it) } ; layoutPanes }

    fun panesOf(table : DVTable) {

        if (table.panes.isNotEmpty()) {
            layoutPanes.addAll( (table.panes.filter { it.cellType == DVLayoutCell.DVLCellTypes.PANE } as List<DVPane>).toMutableList())

            (table.panes.filter { it.cellType == DVLayoutCell.DVLCellTypes.TABLE } as List<DVTable>).forEach { panesOf(it) }
        }
    }

    @Transient
    var layoutTables : MutableList<DVTable> = mutableListOf()

    fun layoutTables() = if (layoutTables.isNotEmpty() ) layoutTables else { layout.forEach { tablesOf(it) } ; layoutTables }

    fun tablesOf(table : DVTable) {

        if (table.panes.isNotEmpty()) {
            layoutTables.add(table)

            (table.panes.filter { it.cellType == DVLayoutCell.DVLCellTypes.TABLE } as List<DVTable>).forEach { tablesOf(it) }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun imagePanes() = layoutPanes().filter { it.paneType == DVPane.DVPaneTypes.IMAGE.tag() } as List<DVImagePane>

    @Suppress("UNCHECKED_CAST")
    fun textPanes() = layoutPanes().filter { it.paneType == DVPane.DVPaneTypes.TEXT.tag() } as List<DVTextPane>

    fun setAdjacencies(fontSize: KcopFont) {
        textPanes().forEach { dvTextPane ->
            dvTextPane.adjacencyTopPadOffset = textAdjacencyTopPads.firstOrNull { it.tag == dvTextPane.tag }?.fontPads?.firstOrNull { it.fontSize() == fontSize }?.yOffset()
            dvTextPane.adjacencyAllowedRows = textAdjacencyRows.firstOrNull { it.tag == dvTextPane.tag }?.fontRows?.firstOrNull { it.fontSize() == fontSize }?.allowRows()
        }
    }

    fun setImagePaneContent(paneTag : String, texture : Texture?) {
        imagePanes().firstOrNull { it.tag == paneTag }?.paneTexture = texture
    }

    fun setTextPaneContent(paneTag : String, text : String? = null, colorOverride : ColorPalette? = null) {
        textPanes().firstOrNull { it.tag == paneTag }?.apply {
            if (text != null) this.paneText = text
            if (colorOverride != null) {
                this.colorOverride = colorOverride
                this.textFieldStyle = KcopSkin.textFieldStyle(DVLayoutHandler.currentFontSize, colorOverride)
                paneTextField.style = this.textFieldStyle
            }
        }
    }

    fun setTextPaneMode(paneTag : String, mode: DVTextPane.DVTextPaneMode, editRegex : String? = null, writeCallback : (String) -> Unit = {}) {
        textPanes().firstOrNull { it.tag == paneTag }?.apply {
            this.mode = mode
            this.writeCallback = writeCallback
            if (editRegex != null) this.editRegex = editRegex
            if (mode == DVTextPane.DVTextPaneMode.WRITE) {
                KcopBase.keyboardFocus = this.paneTextField
            }
        }
    }

    fun fadeImageIn(paneTag : String, texture : Texture?) {
        imagePanes().firstOrNull { it.tag == paneTag }?.fadeImageIn(texture)
    }

    fun fadeImageOut(paneTag : String) {
        imagePanes().firstOrNull { it.tag == paneTag }?.fadeImageOut()
    }

    fun clearContent() {
        clearImagePaneContent()
        clearTextPaneContent()
        clearAlphaPaneContent()
    }

    fun clearImagePaneContent() {
        imagePanes().forEach { dvImagePane -> dvImagePane.paneTexture = null }
    }

    fun clearAlphaPaneContent() {
        layoutPanes().forEach { dvPane -> dvPane.alphaMask = 0f }
    }

    fun clearTextPaneContent() {
        textPanes().forEach { dvTextPane -> dvTextPane.paneText = "" }
    }

    fun setTextLabelStyle(textLabelStyle: Label.LabelStyle) {
        textPanes().forEach { it.textLabelStyle = textLabelStyle }
    }

    fun setTextFieldStyle(textFieldStyle: TextFieldStyle) {
        textPanes().forEach { it.textFieldStyle = textFieldStyle }
    }

    fun setFullTextPaneContent(screenWidth: Float, screenHeight: Float, currentText: String) {

        val textContentMap = mutableMapOf<Int, String?>()

        var textParsed = false

        var currentTextRemaining = currentText
        var currentDVPIdx = 0

        val textDVPs = textPanes()
        var dvpText = currentTextRemaining

        var extraRow: Int

        while (!textParsed && currentText.isNotBlank() && textDVPs.isNotEmpty()) {
            var paneParsed = false

            val dvpPaneHeight = textDVPs[currentDVPIdx].dvpType().height(screenHeight) - 2 * ViewType.padHeight(screenHeight)
            var dvpRows = 0
            extraRow = textDVPs[currentDVPIdx].adjacencyAllowedRows ?: 0

            while (!paneParsed && !textParsed) {
                var rowParsed = false

                while (!rowParsed) {
                    val dvpRowWidth = textDVPs[currentDVPIdx].dvpType().height(screenWidth) - 2 * ViewType.padWidth(screenWidth)

                    var textLabel = Label(dvpText, textDVPs[currentDVPIdx].textLabelStyle)

                    val dvpPaneModHeight = dvpPaneHeight + extraRow * textLabel.height

                    val textLabelSize = textLabel.width //* textLabel.height

                    if (textLabelSize > dvpRowWidth) {
                        val lastSpaceIdx = dvpText.lastIndexOf(" ")
                        dvpText = dvpText.substring(0, lastSpaceIdx)
                    } else {

                        if (dvpText.contains("\n")) {
                            dvpText = dvpText.substring(0, dvpText.indexOf("\n"))
                            //recalc text label, removing \n
                            textLabel = Label(dvpText, textDVPs[currentDVPIdx].textLabelStyle)

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
                            if ((currentDVPIdx == textDVPs.size - 1) && paneParsed) {
                                dvpText += "..."
                                textParsed = true
                            }
                        }

                        if (textContentMap[currentDVPIdx] == null) {
                            textContentMap[currentDVPIdx] = dvpText.trim() + "\n"
                        } else {
                            textContentMap[currentDVPIdx] += dvpText.trim() + "\n"
                        }

                        dvpText = currentTextRemaining

                    }
                }
            }

            currentDVPIdx += 1
        }

        if (textContentMap.isNotEmpty()) {
            textContentMap.entries.forEach { textContentMapEntry -> setTextPaneContent(textDVPs[textContentMapEntry.key].tag!!, textContentMapEntry.value) }
        }
    }

    companion object {
        const val DvLayoutEmptyTag = "emptyLayout"
        const val DvLayoutRootTableTag = "dvLayout" //main displayView (to the left)
        const val DavLayoutRootTableTag = "davLayout" //aux displayView (to the right)

        fun dvLayout() = DVLayout(name= DvLayoutEmptyTag)
    }
}

