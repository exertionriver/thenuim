package river.exertion.kcop.simulation.view.displayViewLayouts

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.assets.FontSize
import river.exertion.kcop.assets.KcopSkin
import river.exertion.kcop.simulation.view.DisplayViewPane
import river.exertion.kcop.simulation.view.FontPackage
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.view.SdcHandler

class DVLBasicPictureNarrative(override var screenWidth: Float, override var screenHeight: Float) : Telegraph, DisplayViewLayout {

    init {
        MessageChannel.SDC_BRIDGE.enableReceive(this)
        MessageChannel.KCOP_SKIN_BRIDGE.enableReceive(this)
    }

    override val tag = Companion.tag

    override lateinit var sdcHandler : SdcHandler
    override lateinit var kcopSkin : KcopSkin

    override val paneTextures : MutableMap<Int, Texture?> = mutableMapOf()
    override val paneTextureMaskAlpha : MutableMap<Int, Float?> = mutableMapOf()

    override val paneRefiners : MutableMap<Int, Vector2?> = mutableMapOf(
        0 to Vector2(1f, 0f),
        1 to Vector2(3f, 0f),
        2 to Vector2(4f, 1f)
    )

    override fun adjacencyPaneRows(currentFontSize: FontSize) : MutableMap<Int, Int?> = mutableMapOf(
    when (currentFontSize) {
        FontSize.SMALL -> 0 to 2
        FontSize.MEDIUM -> 0 to 1
        FontSize.LARGE -> 0 to 1
        else -> 0 to 0
    })

    override fun adjacencyTopPadOffset(currentFontSize: FontSize) : MutableMap<Int, Int?> = mutableMapOf(
    when (currentFontSize) {
        FontSize.SMALL -> 2 to -9
        FontSize.MEDIUM -> 2 to -15
        FontSize.LARGE -> 2 to 7
        else -> 2 to 0
    })

    override fun definePanes() : MutableMap<Int, DisplayViewPane> {
        val panes : MutableMap<Int, DisplayViewPane> = mutableMapOf()

        //picture
        panes[0] = DisplayViewPane.LARGE_BY_LARGE

        //text blocks
        panes[1] = DisplayViewPane.TITLE_BY_LARGE
        panes[2] = DisplayViewPane.FULL_BY_TITLE

        return panes
    }

    override fun imagePanes() : List<Int> = listOf(0)
    override fun textPanes() : List<Int> = listOf(1, 2)

    override fun buildPaneTable(layoutMode : Boolean, currentText : String, currentFontSize: FontSize) : Table {

        val panes = buildPaneCtrls(layoutMode, currentText, currentFontSize)

        val innerTable = Table()

        val tlRow0 = Table()
        tlRow0.add(panes[0])
        tlRow0.add(panes[1])

        val tlFull = Table()
        tlFull.add(tlRow0)
        tlFull.row()
        tlFull.add(panes[2])

        innerTable.add(tlFull)
        innerTable.validate()
        innerTable.layout()
        return innerTable
    }

    @Suppress("NewApi")
    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                (MessageChannel.SDC_BRIDGE.isType(msg.message) ) -> {
                    sdcHandler = MessageChannel.SDC_BRIDGE.receiveMessage(msg.extraInfo)
                    return true
                }
                (MessageChannel.KCOP_SKIN_BRIDGE.isType(msg.message) ) -> {
                    kcopSkin = MessageChannel.KCOP_SKIN_BRIDGE.receiveMessage(msg.extraInfo)
                    return true
                }
            }
        }
        return false
    }

    companion object {
        const val tag = "basicPictureNarrative"
    }
}
