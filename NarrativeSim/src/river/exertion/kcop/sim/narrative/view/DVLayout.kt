package river.exertion.kcop.sim.narrative.view

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Label
import kotlinx.serialization.Serializable
import river.exertion.kcop.messaging.Id
import river.exertion.kcop.view.FontSize
import river.exertion.kcop.view.layout.ViewType

@Serializable
data class DVLayout(
    override var id: String = Id.randomId(),
    val name : String,
    val layout : MutableList<DVTable> = mutableListOf(),
    val textAdjacencyRows: MutableList<DVLTextAdjacencyRow> = mutableListOf(),
    val textAdjacencyTopPads: MutableList<DVLTextAdjacencyTopPad> = mutableListOf()
) : Id {

    @Suppress("UNCHECKED_CAST")
    fun panes() = layout.flatMap { it.panes }.filter { it.cellType == DVLayoutCell.DVLCellTypes.PANE } as List<DVPane>

    @Suppress("UNCHECKED_CAST")
    fun imagePanes() = panes().filter { it.paneType == DVPane.DVPaneTypes.IMAGE.tag() } as List<DVImagePane>

    @Suppress("UNCHECKED_CAST")
    fun textPanes() = panes().filter { it.paneType == DVPane.DVPaneTypes.TEXT.tag() } as List<DVTextPane>

    fun setAdjacencies(fontSize: FontSize) {
        textPanes().forEach { dvTextPane ->
            dvTextPane.adjacencyTopPadOffset = textAdjacencyTopPads.firstOrNull { it.idx == dvTextPane.idx }?.fontPads?.firstOrNull { it.fontSize() == fontSize }?.yOffset()
                    ?: 0
            dvTextPane.adjacencyAllowedRows = textAdjacencyRows.firstOrNull { it.idx == dvTextPane.idx }?.fontRows?.firstOrNull { it.fontSize() == fontSize }?.allowRows()
                    ?: 0
        }
    }

    fun setImagePaneContent(paneIdx : Int, texture : Texture?) {
        imagePanes().firstOrNull { it.idx() == paneIdx }?.paneTexture = texture
    }

    fun fadeImageIn(paneIdx : Int, texture : Texture?) {
        imagePanes().firstOrNull { it.idx() == paneIdx }?.fadeImageIn(texture)
    }

    fun fadeImageOut(paneIdx : Int, texture : Texture?) {
        imagePanes().firstOrNull { it.idx() == paneIdx }?.fadeImageOut(texture)
    }

    fun clearImagePaneContent() {
        imagePanes().forEach { dvImagePane -> dvImagePane.paneTexture = null }
    }

    fun clearAlphaPaneContent() {
        panes().forEach { dvPane -> dvPane.alphaMask = 1f }
    }

    fun setTextLabelStyle(textLabelStyle: Label.LabelStyle) {
        textPanes().forEach { it.textLabelStyle = textLabelStyle }
    }

    fun setTextPaneContent(screenWidth: Float, screenHeight: Float, currentText: String) {

        val textContentMap = mutableMapOf<Int, String?>()

        var textParsed = false

        var currentTextRemaining = currentText
        var currentDVPIdx = 0

        val textDVPs = textPanes()
        var dvpText = currentTextRemaining

        var extraRow: Int

        while (!textParsed && currentText.isNotBlank() && textDVPs.isNotEmpty()) {
            var paneParsed = false

            val dvpPaneHeight = (textDVPs[currentDVPIdx].dvpType().height(screenHeight) - 2 * ViewType.padHeight(screenHeight))
            var dvpRows = 0
            extraRow = textDVPs[currentDVPIdx].adjacencyAllowedRows ?: 0

            while (!paneParsed && !textParsed) {
                var rowParsed = false

                while (!rowParsed) {
                    val dvpRowWidth = (textDVPs[currentDVPIdx].dvpType().height(screenWidth) - 2 * ViewType.padWidth(screenWidth))

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
            textPanes().forEachIndexed { idx, dvTextPane -> dvTextPane.paneText = textContentMap[idx] }
        }
    }

    fun clearTextPaneContent() {
        textPanes().forEach { dvTextPane -> dvTextPane.paneText = "" }
    }

    companion object {
        const val DvLayoutTag = "emptyLayout"
        fun dvLayout() = DVLayout(name= DvLayoutTag)
    }
}

