package river.exertion.kcop.simulation.colorPalette

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.Align
import ktx.actors.onClick
import ktx.style.label
import river.exertion.kcop.assets.KcopSkin
import river.exertion.kcop.simulation.view.FontPackage
import river.exertion.kcop.system.colorPalette.ColorPalette
import river.exertion.kcop.system.colorPalette.ColorPaletteMessage
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.view.SdcHandler

class ColorSwatchesCtrl(var topX: Float = 0f, var topY: Float = 0f, var swatchWidth: Float = 50f, var swatchHeight: Float = 50f) : Table(), Telegraph {

    init {
        MessageChannel.SDC_BRIDGE.enableReceive(this)
        MessageChannel.KCOP_SKIN_BRIDGE.enableReceive(this)
    }

    lateinit var sdcHandler : SdcHandler
    lateinit var kcopSkin : KcopSkin

    var swatchEntries: Map<String, ColorPalette> = mapOf()

    fun tableWidth() = swatchWidth
    fun tableHeight() = swatchHeight * swatchEntries.size
    fun tablePosX() = topX
    fun tablePosY() = topY - swatchHeight * (swatchEntries.size - 1)

    fun clearTable(heightOverride : Float = tableHeight(), posYOverride : Float = tablePosY()) {
        this.clearChildren()

        this.setSize(tableWidth(), heightOverride)
        this.setPosition(tablePosX(), posYOverride)

//        this.debug()
    }

    fun create() {
        clearTable()

        swatchEntries.entries.forEachIndexed { idx, colorPaletteEntry ->
            addSwatch(idx, colorPaletteEntry)
        }
    }

    fun createWithHeader(headerColorName : String, headerColor : ColorPalette) {
        clearTable(tableHeight() + 2 * swatchHeight, tablePosY() - 2 * swatchHeight)

        addSwatch(0, mapOf(headerColorName to headerColor).entries.first() )
        addSwatch(1, mapOf(null to ColorPalette.Color000).entries.first() )

        swatchEntries.entries.forEachIndexed { idx, colorPaletteEntry ->
            addSwatch(idx + 2, colorPaletteEntry)
        }
    }

    fun addSwatch(idx : Int, colorPaletteEntry : Map.Entry<String?, ColorPalette>) {

        val swatchTexture = sdcHandler.get("cpSwatch_${colorPaletteEntry.key}", colorPaletteEntry.value).textureRegion().apply {
            this.setRegion(topX.toInt(), (topY - swatchHeight * idx).toInt(), swatchWidth.toInt(), swatchHeight.toInt())
        }

        val swatchImg = Image(swatchTexture)

        val stack = Stack()

        if (colorPaletteEntry.key != null) {
            stack.onClick {
                MessageChannel.COLOR_PALETTE_BRIDGE.send(null, ColorPaletteMessage(ColorPaletteMessage.ColorPaletteMessageType.SetBaseColor, colorPaletteEntry.key!!, colorPaletteEntry.value))
            }
        }

        val swatchLabel = Label(colorPaletteEntry.key, skin.apply { this.label { this.font = kcopSkin.fontPackage.text; this.fontColor = colorPaletteEntry.value.label().color()  }})
        swatchLabel.setAlignment(Align.center)

        stack.add(swatchImg)
        stack.add(swatchLabel)
        this.add(stack)
        this.row()
    }

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                (MessageChannel.SDC_BRIDGE.isType(msg.message) ) -> {
                    sdcHandler = MessageChannel.SDC_BRIDGE.receiveMessage(msg.extraInfo)
                    return true
                }
                (MessageChannel.KCOP_SKIN_BRIDGE.isType(msg.message) ) -> {
                    kcopSkin = MessageChannel.KCOP_SKIN_BRIDGE.receiveMessage(msg.extraInfo)
                    super.setSkin(kcopSkin.skin)
                    return true
                }
            }
        }
        return false
    }

    fun dispose() {
        sdcHandler.dispose()
    }
}