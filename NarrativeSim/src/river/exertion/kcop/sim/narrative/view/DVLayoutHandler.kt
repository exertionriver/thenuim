package river.exertion.kcop.sim.narrative.view

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import river.exertion.kcop.asset.view.ColorPalette
import river.exertion.kcop.asset.view.FontSize
import river.exertion.kcop.asset.view.KcopSkin
import river.exertion.kcop.view.SdcHandler

object DVLayoutHandler : Telegraph {

    lateinit var sdcHandler : SdcHandler
    lateinit var kcopSkin: KcopSkin

    var currentLayoutMode = false
    var currentText = ""
    var currentFontSize = FontSize.SMALL
    var currentDvLayout : DVLayout = DVLayout.dvLayout()

    fun paneColorTexture(dvPane: DVPane, overrideColor : ColorPalette?) : TextureRegion {
        return sdcHandler.get("pane_${dvPane.idx}", overrideColor ?: KcopSkin.BackgroundColor).textureRegion().apply {
            this.setRegion(0, 0, dvPane.dvpType().width(KcopSkin.screenWidth).toInt() - 1, dvPane.dvpType().height(KcopSkin.screenHeight).toInt() - 1)
        }
    }

    fun paneBATexture(dvPane: DVPane) : TextureRegion {
        return sdcHandler.getBlackAlpha("bapane_${dvPane.idx}", dvPane.alphaMask).textureRegion().apply {
            this.setRegion(0, 0, dvPane.dvpType().width(KcopSkin.screenWidth).toInt() - 1, dvPane.dvpType().height(KcopSkin.screenHeight).toInt() - 1)
        }
    }

    fun setImagePaneContent(paneIdx : Int, texture : Texture?) = currentDvLayout.setImagePaneContent(paneIdx, texture)

    fun fadeImageIn(paneIdx : Int, texture : Texture?) = currentDvLayout.fadeImageIn(paneIdx, texture)

    fun fadeImageOut(paneIdx : Int, texture : Texture?) = currentDvLayout.fadeImageOut(paneIdx, texture)

    fun buildPaneContent() : DVPaneContent {

        val paneContent = DVPaneContent()
        currentDvLayout.setAdjacencies(currentFontSize)

        if (currentLayoutMode) {

            currentDvLayout.panes().forEach { dvPane ->
                val randomColor = ColorPalette.randomW3cBasic()
                val randomColorImage = Image(TextureRegionDrawable(paneColorTexture(dvPane, randomColor).texture))
                val randomColorLabelStyle = kcopSkin.labelStyle(FontSize.TEXT, randomColor)

                when (dvPane) {
                    is DVImagePane -> paneContent.data[dvPane.idx()] = dvPane.layoutPane(KcopSkin.screenWidth, KcopSkin.screenHeight, randomColorImage, randomColorLabelStyle)
                    is DVTextPane -> paneContent.data[dvPane.idx()] = dvPane.layoutPane(KcopSkin.screenWidth, KcopSkin.screenHeight, randomColorImage, randomColorLabelStyle)
                }
            }

        } else {
            val textLabelStyle = kcopSkin.labelStyle(currentFontSize)

            currentDvLayout.setTextLabelStyle(textLabelStyle)
            currentDvLayout.setTextPaneContent(KcopSkin.screenWidth, KcopSkin.screenHeight, currentText)

            currentDvLayout.panes().forEach { dvPane ->
                paneContent.data[dvPane.idx()] = Stack().apply {
                    this.add(dvPane.emptyPane(KcopSkin.screenWidth, KcopSkin.screenHeight))
                    this.add(dvPane.contentPane(KcopSkin.screenWidth, KcopSkin.screenHeight))
                    this.add(dvPane.alphaPane(KcopSkin.screenWidth, KcopSkin.screenHeight, Image(TextureRegionDrawable(paneBATexture(dvPane)))))
                }
            }
        }

        return paneContent
    }

    fun buildLayout() : Table {

        val paneContent = buildPaneContent()
        val layoutTable = Table()

        currentDvLayout.layout.firstOrNull{ it.tableIdx == "layout" }?.panes?.forEach { dvPane ->
            layoutTable.paneCell(currentDvLayout, paneContent, dvPane)
        }

        layoutTable.validate()

        return layoutTable
    }

    fun Table.paneCell(dvLayout: DVLayout, paneContent : DVPaneContent, dvLayoutCell: DVLayoutCell) {

        when {
            (dvLayoutCell is DVPane) -> { this.add ( paneContent.data.entries.firstOrNull { it.key == dvLayoutCell.idx() }?.value ) }
            (dvLayoutCell is DVRow) -> { this.row() }
            (dvLayoutCell is DVTable) -> { this.add( Table().apply {
                    dvLayout.layout.firstOrNull { it.tableIdx == dvLayoutCell.tableIdx }?.panes?.forEach {
                        dvPane -> this.paneCell(dvLayout, paneContent, dvPane)
                    }
                }).colspan(dvLayoutCell.colspan())
            }
        }
    }

    @Suppress("NewApi")
    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                else -> return false
            }
        }
        return false
    }

    fun dispose() {
        sdcHandler.dispose()
        kcopSkin.dispose()
    }
}
