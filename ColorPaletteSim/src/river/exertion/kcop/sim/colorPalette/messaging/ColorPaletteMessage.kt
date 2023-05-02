package river.exertion.kcop.sim.colorPalette.messaging

import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.view.ColorPalette

data class ColorPaletteMessage(val colorPaletteMessageType: ColorPaletteMessageType, val baseColorName : String? = null, val baseCp: ColorPalette? = null, val modifyType: ColorPaletteModifyType? = null) {

    init {
        MessageChannelHandler.addChannel(MessageChannel(ColorPaletteBridge, this::class))
    }

    enum class ColorPaletteMessageType {
        SetBaseColor, ModifyBaseColor
    }

    enum class ColorPaletteModifyType {
        ColorBaseIncrR, ColorBaseIncrG, ColorBaseIncrB,
        ColorBaseDecrR, ColorBaseDecrG, ColorBaseDecrB,
        ColorBaseIncr, ColorBaseDecr,
        ColorSamplePrev, ColorSampleNext
    }

    companion object {
        const val ColorPaletteBridge = "ColorPaletteBridge"
    }
}