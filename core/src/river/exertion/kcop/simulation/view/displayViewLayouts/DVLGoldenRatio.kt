package river.exertion.kcop.simulation.view.displayViewLayouts

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.assets.FontSize
import river.exertion.kcop.assets.KcopSkin
import river.exertion.kcop.simulation.view.DisplayViewPaneType
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.view.SdcHandler

class DVLGoldenRatio(override var screenWidth: Float, override var screenHeight: Float) : Telegraph, IDisplayViewLayout {

    init {
        MessageChannel.SDC_BRIDGE.enableReceive(this)
        MessageChannel.KCOP_SKIN_BRIDGE.enableReceive(this)
    }

    override val tag = Companion.tag

    override var currentLayoutMode = false
    override var currentText = ""
    override var currentFontSize = FontSize.SMALL

    override lateinit var sdcHandler : SdcHandler
    override lateinit var kcopSkin: KcopSkin

    override val paneTextures : MutableMap<Int, Texture?> = mutableMapOf()
    override val paneTextureMaskAlpha : MutableMap<Int, Float?> = mutableMapOf()

    override val paneRefiners : MutableMap<Int, Vector2?> = mutableMapOf(
        0 to Vector2(1f, 1f),
        1 to Vector2(1f, 1f),
        2 to Vector2(1f, 0f),
        3 to Vector2(1f, 1f),
        5 to Vector2(0f, 1f),
        6 to Vector2(1f, 0f),
        7 to Vector2(1f, 0f),
        8 to Vector2(0f, 1f),
        9 to Vector2(1f, 0f),
        10 to Vector2(1f, 1f),
        11 to Vector2(1f, 1f),
        12 to Vector2(1f, 0f),
        13 to Vector2(0f, 1f),
        14 to Vector2(1f, 0f),
        15 to Vector2(1f, 1f),
        16 to Vector2(0f, 1f),
        19 to Vector2(0f, 1f),
        21 to Vector2(0f, 1f),
    )

    override fun adjacencyPaneRows(currentFontSize: FontSize) : MutableMap<Int, Int?> = mutableMapOf()
    override fun adjacencyTopPadOffset(currentFontSize: FontSize) : MutableMap<Int, Int?> = mutableMapOf()

    override fun definePanes() : DisplayViewPanes {
        val panes = DisplayViewPanes()

        panes.data[0] = DisplayViewPaneType.MEDIUM_BY_MEDIUM
        panes.data[1] = DisplayViewPaneType.LARGE_BY_LARGE
        panes.data[2] = DisplayViewPaneType.LARGE_BY_LARGE
        panes.data[3] = DisplayViewPaneType.LARGE_BY_LARGE
        panes.data[4] = DisplayViewPaneType.MEDIUM_BY_MEDIUM
        panes.data[5] = DisplayViewPaneType.MEDIUM_BY_MEDIUM
        panes.data[6] = DisplayViewPaneType.MEDIUM_BY_MEDIUM
        panes.data[7] = DisplayViewPaneType.MEDIUM_BY_MEDIUM
        panes.data[8] = DisplayViewPaneType.MEDIUM_BY_MEDIUM
        panes.data[9] = DisplayViewPaneType.SMALL_BY_SMALL
        panes.data[10] = DisplayViewPaneType.SMALL_BY_SMALL
        panes.data[11] = DisplayViewPaneType.SMALL_BY_SMALL
        panes.data[12] = DisplayViewPaneType.SMALL_BY_SMALL
        panes.data[13] = DisplayViewPaneType.SMALL_BY_SMALL
        panes.data[14] = DisplayViewPaneType.SMALL_BY_SMALL
        panes.data[15] = DisplayViewPaneType.SMALL_BY_SMALL
        panes.data[16] = DisplayViewPaneType.TINY_BY_TINY
        panes.data[17] = DisplayViewPaneType.TINY_BY_TINY
        panes.data[18] = DisplayViewPaneType.TINY_BY_TINY
        panes.data[19] = DisplayViewPaneType.TINY_BY_TINY
        panes.data[20] = DisplayViewPaneType.TINY_BY_TINY
        panes.data[21] = DisplayViewPaneType.TINY_BY_TINY
        panes.data[22] = DisplayViewPaneType.UNIT_BY_UNIT
        panes.data[23] = DisplayViewPaneType.UNIT_BY_UNIT
        panes.data[24] = DisplayViewPaneType.UNIT_BY_UNIT
        panes.data[25] = DisplayViewPaneType.UNIT_BY_UNIT
        panes.data[26] = DisplayViewPaneType.UNIT_BY_UNIT
        panes.data[27] = DisplayViewPaneType.UNIT_BY_UNIT
        panes.data[28] = DisplayViewPaneType.UNIT_BY_UNIT
        panes.data[29] = DisplayViewPaneType.UNIT_BY_UNIT
        panes.data[30] = DisplayViewPaneType.UNIT_BY_UNIT
        panes.data[31] = DisplayViewPaneType.UNIT_BY_UNIT
        panes.data[32] = DisplayViewPaneType.UNIT_BY_UNIT
        panes.data[33] = DisplayViewPaneType.UNIT_BY_UNIT

        return panes
    }

    override fun imagePanes() : List<Int> = definePanes().data.keys.toList()
    override fun textPanes() : List<Int> = listOf()

    override fun buildPaneTable() : Table {

        val panes = buildPaneCtrls()

        val innerTable = Table()

        val tlSpiral0 = Table()
        tlSpiral0.add(panes[30])
        tlSpiral0.add(panes[24])

        val tlSpiral1 = Table()
        tlSpiral1.add(tlSpiral0)
        tlSpiral1.row()
        tlSpiral1.add(panes[18])

        val tlSpiral2 = Table()
        tlSpiral2.add(tlSpiral1)
        tlSpiral2.add(panes[11])

        val tlSpiral3 = Table()
        tlSpiral3.add(panes[7])
        tlSpiral3.row()
        tlSpiral3.add(tlSpiral2)

        val tlSpiral4 = Table()
        tlSpiral4.add(panes[2])
        tlSpiral4.add(tlSpiral3)

        innerTable.add(tlSpiral4).colspan(13)

        val trSpiral0a = Table()
        trSpiral0a.add(panes[33])
        trSpiral0a.row()
        trSpiral0a.add(panes[27])

        val trSpiral0b = Table()
        trSpiral0b.add(panes[26])
        trSpiral0b.add(panes[32])

        val trSpiral1a = Table()
        trSpiral1a.add(panes[21])
        trSpiral1a.add(trSpiral0a)

        val trSpiral1b = Table()
        trSpiral1b.add(trSpiral0b)
        trSpiral1b.row()
        trSpiral1b.add(panes[20])

        val trSpiral2a = Table()
        trSpiral2a.add(panes[14])
        trSpiral2a.row()
        trSpiral2a.add(trSpiral1a)
        trSpiral2a.row()
        trSpiral2a.add(panes[15])

        val trSpiral2b = Table()
        trSpiral2b.add(trSpiral1b)
        trSpiral2b.add(panes[13])

        val trSpiral3b = Table()
        trSpiral3b.add(panes[4])
        trSpiral3b.row()
        trSpiral3b.add(trSpiral2b)

        val trSpiral4 = Table()
        trSpiral4.add(trSpiral2a)
        trSpiral4.add(trSpiral3b)

        innerTable.add(trSpiral4).colspan(8)
        innerTable.row()

        val lSpiral0 = Table()
        lSpiral0.add(panes[28])
        lSpiral0.row()
        lSpiral0.add(panes[22])

        val lSpiral1 = Table()
        lSpiral1.add(lSpiral0)
        lSpiral1.add(panes[16])

        val lSpiral2 = Table()
        lSpiral2.add(panes[9])
        lSpiral2.row()
        lSpiral2.add(lSpiral1)

        val lSpiral3 = Table()
        lSpiral3.add(panes[5])
        lSpiral3.add(lSpiral2)

        val rSpiral0 = Table()
        rSpiral0.add(panes[31])
        rSpiral0.row()
        rSpiral0.add(panes[25])

        val rSpiral1 = Table()
        rSpiral1.add(panes[19])
        rSpiral1.add(rSpiral0)

        val rSpiral2 = Table()
        rSpiral2.add(rSpiral1)
        rSpiral2.row()
        rSpiral2.add(panes[12])

        val rSpiral3 = Table()
        rSpiral3.add(rSpiral2)
        rSpiral3.add(panes[8])

        innerTable.add(lSpiral3).colspan(8)
        innerTable.add(panes[0]).colspan(5)
        innerTable.add(rSpiral3).colspan(8)

        innerTable.row()

        val brSpiral0 = Table()
        brSpiral0.add(panes[23])
        brSpiral0.add(panes[29])

        val brSpiral1 = Table()
        brSpiral1.add(brSpiral0)
        brSpiral1.row()
        brSpiral1.add(panes[17])

        val brSpiral2 = Table()
        brSpiral2.add(panes[10])
        brSpiral2.add(brSpiral1)

        val brSpiral3 = Table()
        brSpiral3.add(brSpiral2)
        brSpiral3.row()
        brSpiral3.add(panes[6])

        innerTable.add(panes[1]).colspan(8)
        innerTable.add(brSpiral3).colspan(5)
        innerTable.add(panes[3]).colspan(8)

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
        const val tag = "goldenRatioLayout"
    }
}
