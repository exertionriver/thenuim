package river.exertion.thenuim.view.layout.displayViewLayout

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import river.exertion.thenuim.asset.view.ColorPalette
import river.exertion.thenuim.view.IDisplayViewLayoutHandler
import river.exertion.thenuim.view.TnmSkin
import river.exertion.thenuim.view.SdcHandler
import river.exertion.thenuim.view.TnmFont
import river.exertion.thenuim.view.TnmSkin.screenHeight
import river.exertion.thenuim.view.TnmSkin.screenWidth
import river.exertion.thenuim.view.layout.AudioView
import river.exertion.thenuim.view.layout.DisplayAuxView
import river.exertion.thenuim.view.layout.DisplayView

object DVLayoutHandler : IDisplayViewLayoutHandler {

    var currentText = ""
    var currentFontSize = TnmFont.SMALL
    var currentFontColor = ColorPalette.randomW3cBasic()
    var currentDvLayout : DVLayout = DVLayout.dvLayout()

    fun paneColorTexture(dvPane: DVPane, overrideColor : ColorPalette?) : TextureRegion {
        return SdcHandler.updorad("pane_${dvPane.tag}", overrideColor ?: TnmSkin.BackgroundColor).textureRegion().apply {
            this.setRegion(0, 0, dvPane.dvpType().width(screenWidth).toInt() - 1, dvPane.dvpType().height(screenHeight).toInt() - 1)
        }
    }

    fun paneBATexture(dvPane: DVPane) : TextureRegion {
        return SdcHandler.updoradBackgroundAlpha("bapane_${dvPane.tag}", dvPane.alphaMask).textureRegion().apply {
            this.setRegion(0, 0, dvPane.dvpType().width(screenWidth).toInt() - 1, dvPane.dvpType().height(screenHeight).toInt() - 1)
        }
    }

    fun showImage(paneTag : String, texture : Texture) = currentDvLayout.setImagePaneContent(paneTag, texture)

    fun hideImage(paneTag : String) = currentDvLayout.setImagePaneContent(paneTag, null)

    fun fadeImageIn(paneTag : String, texture : Texture?) = currentDvLayout.fadeImageIn(paneTag, texture)

    fun fadeImageOut(paneTag : String) = currentDvLayout.fadeImageOut(paneTag)

    fun buildPaneContent() : DVPaneContent {

        val paneContent = DVPaneContent()
        currentDvLayout.setAdjacencies(currentFontSize)

        if (TnmSkin.displayMode) {

            currentDvLayout.layoutPanes().forEach { dvPane ->
                val randomColor = ColorPalette.randomW3cBasic()
                val randomColorImage = Image(TextureRegionDrawable(paneColorTexture(dvPane, randomColor).texture))
                val randomColorLabelStyle = TnmSkin.labelStyle(TnmFont.TEXT, randomColor.label())

                when (dvPane) {
                    is DVImagePane -> paneContent.data[dvPane.tag!!] = dvPane.layoutPane(screenWidth, screenHeight, randomColorImage, randomColorLabelStyle)
                    is DVTextPane -> paneContent.data[dvPane.tag!!] = dvPane.layoutPane(screenWidth, screenHeight, randomColorImage, randomColorLabelStyle)
                }
            }

        } else {
            val textLabelStyle = TnmSkin.labelStyle(currentFontSize, currentFontColor)
            val textFieldStyle = TnmSkin.textFieldStyle(currentFontSize, currentFontColor)

            currentDvLayout.setTextLabelStyle(textLabelStyle)
            currentDvLayout.setTextFieldStyle(textFieldStyle)

            if (currentText.isNotEmpty())
                currentDvLayout.setFullTextPaneContent(screenWidth, screenHeight, currentText)

            currentDvLayout.layoutPanes().forEach { dvPane ->
                paneContent.data[dvPane.tag!!] = Stack().apply {
                    this.add(dvPane.emptyPane(screenWidth, screenHeight))
                    this.add(dvPane.contentPane(screenWidth, screenHeight))
                    //this.add(dvPane.alphaPane(KcopSkin.screenWidth, KcopSkin.screenHeight, Image(TextureRegionDrawable(
                    //    paneBATexture(dvPane)
                   // ))))
                }//.apply { this.onClick { println("hello world!") } }
            }
        }

        return paneContent
    }

    fun Table.paneCell(dvLayout: DVLayout, paneContent : DVPaneContent, dvLayoutCell: DVLayoutCell) {

        when {
            (dvLayoutCell is DVPane) -> { this.add ( paneContent.data.entries.firstOrNull { it.key == dvLayoutCell.tag }?.value ) }
            (dvLayoutCell is DVRow) -> { this.row() }
            (dvLayoutCell is DVTable) -> { this.add( Table().apply {
                dvLayout.layoutTables().firstOrNull { it.tableTag == dvLayoutCell.tableTag }?.panes?.forEach {
                        dvPane -> this.paneCell(dvLayout, paneContent, dvPane)
                }
                if (dvLayoutCell.width != null) this.width = dvLayoutCell.dvpType().width(screenWidth)
                if (dvLayoutCell.height != null) this.height = dvLayoutCell.dvpType().height(screenHeight)
            }).colspan(dvLayoutCell.colspan())
            }
        }
    }

    override fun build() {

        if (currentDvLayout.layout.isNotEmpty()) {
            val paneContent = buildPaneContent()

            DisplayView.displayViewTable.clearChildren()

            currentDvLayout.layout.firstOrNull{ it.tableTag == DVLayout.DvLayoutRootTableTag }?.panes?.forEach { dvPane ->
                DisplayView.displayViewTable.paneCell(currentDvLayout, paneContent, dvPane)
            }

            DisplayAuxView.displayViewTable.clearChildren()

            currentDvLayout.layout.firstOrNull{ it.tableTag == DVLayout.DavLayoutRootTableTag }?.panes?.forEach { dvPane ->
                DisplayAuxView.displayViewTable.paneCell(currentDvLayout, paneContent, dvPane)
            }
        }

        if (TnmSkin.displayMode) DisplayView.displayViewTable.debug()
        if (TnmSkin.displayMode) DisplayAuxView.displayViewTable.debug()

        DisplayView.build()
        DisplayAuxView.build()
    }

    override fun clearContent() {
        AudioView.stopMusic()
        currentText = ""
        currentDvLayout.clearContent()
        currentDvLayout.layout.clear()
        DisplayView.displayViewTable.clearChildren()
        DisplayAuxView.displayViewTable.clearChildren()
        build()
    }
}
