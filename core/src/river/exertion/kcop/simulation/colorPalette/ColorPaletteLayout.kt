package river.exertion.kcop.simulation.colorPalette

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.assets.FontSize
import river.exertion.kcop.simulation.view.FontPackage
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.colorPalette.ColorPalette
import river.exertion.kcop.system.colorPalette.ColorPaletteMessage
import river.exertion.kcop.simulation.view.ViewType
import kotlin.reflect.jvm.javaMethod

class ColorPaletteLayout(var width : Float, var height : Float) : Telegraph {

    init {
        MessageChannel.COLOR_PALETTE_BRIDGE.enableReceive(this)
    }

    var baseColorName = "darkGray"
    var baseColor = ColorPalette.of(baseColorName)

    var sampleSwatchesIdx = 0
    var sampleSwatchesMinIdx = 0
    var sampleSwatchesMaxIdx = ColorPalette.w3cColors().size - 1

    var sampleSwatchesCtrl = ColorSwatchesCtrl(firstColorColumn(), firstColorRow(), colorSwatchWidth(), colorSwatchHeight())
    var baseSwatchesCtrl = ColorSwatchesCtrl(secondColorColumn(), firstColorRow(), colorSwatchWidth(), colorSwatchHeight())
    var compSwatchesCtrl = ColorSwatchesCtrl(thirdColorColumn(), firstColorRow(), colorSwatchWidth(), colorSwatchHeight())
    var triadFirstSwatchesCtrl = ColorSwatchesCtrl(fourthColorColumn(), firstColorRow(), colorSwatchWidth(), colorSwatchHeight())
    var triadSecondSwatchesCtrl = ColorSwatchesCtrl(fifthColorColumn(), firstColorRow(), colorSwatchWidth(), colorSwatchHeight())

    fun colorColumnWidth() = ViewType.fifthWidth(width) + ViewType.padWidth(width)

    fun firstColorColumn() = ViewType.seventhWidth(width)
    fun secondColorColumn() = firstColorColumn() + colorColumnWidth()
    fun thirdColorColumn() = secondColorColumn() + colorColumnWidth()
    fun fourthColorColumn() = thirdColorColumn() + colorColumnWidth()
    fun fifthColorColumn() = fourthColorColumn() + colorColumnWidth()

    fun colorRowHeight() = ViewType.seventhHeight(height)

    fun firstColorRow() = ViewType.firstHeight(height) - colorRowHeight()

    fun colorSwatchHeight() = colorRowHeight()
    fun colorSwatchWidth() = colorColumnWidth()

    fun createSampleSwatchesCtrl() : Table {
        sampleSwatchesCtrl.swatchEntries = ColorPalette.w3cColors()[sampleSwatchesIdx]
        sampleSwatchesCtrl.create()

        return sampleSwatchesCtrl
    }

    fun colorSamplePrev() {
        if (sampleSwatchesIdx == sampleSwatchesMinIdx) sampleSwatchesIdx = sampleSwatchesMaxIdx else sampleSwatchesIdx--

        createSampleSwatchesCtrl()
    }

    fun colorSampleNext() {
        if (sampleSwatchesIdx == sampleSwatchesMaxIdx) sampleSwatchesIdx = sampleSwatchesMinIdx else sampleSwatchesIdx++

        createSampleSwatchesCtrl()
    }

    private fun createSpectrumSwatchesCtrl(swatchesCtrl : ColorSwatchesCtrl, colorPalette : ColorPalette, colorNameOverride : String? = null) : Table {
        val colorName = colorNameOverride ?: colorPalette.tags()[0]

        swatchesCtrl.swatchEntries = colorPalette.labelledSpectrum(colorName)
        swatchesCtrl.createWithHeader(colorName, colorPalette)

        return swatchesCtrl
    }

    private fun recreateSpectrumSwatches() {
        createBaseSwatchesCtrl()
        createCompSwatchesCtrl()
        createTriadFirstSwatchesCtrl()
        createTriadSecondSwatchesCtrl()
    }

    fun createBaseSwatchesCtrl() = createSpectrumSwatchesCtrl(baseSwatchesCtrl, baseColor, baseColorName)
    fun createCompSwatchesCtrl() = createSpectrumSwatchesCtrl(compSwatchesCtrl, baseColor.comp())
    fun createTriadFirstSwatchesCtrl() = createSpectrumSwatchesCtrl(triadFirstSwatchesCtrl, baseColor.triad().first)
    fun createTriadSecondSwatchesCtrl() = createSpectrumSwatchesCtrl(triadSecondSwatchesCtrl, baseColor.triad().second)

    private fun setColorBase(colorPalette: ColorPalette) {
        baseColor = colorPalette
        baseColorName = baseColor.tags()[0]

        recreateSpectrumSwatches()
    }

    fun colorBaseIncr() = setColorBase(baseColor.incr())
    fun colorBaseDecr() = setColorBase(baseColor.decr())
    fun colorBaseIncrR() = setColorBase(baseColor.incrR())
    fun colorBaseIncrG() = setColorBase(baseColor.incrG())
    fun colorBaseIncrB() = setColorBase(baseColor.incrB())
    fun colorBaseDecrR() = setColorBase(baseColor.decrR())
    fun colorBaseDecrG() = setColorBase(baseColor.decrG())
    fun colorBaseDecrB() = setColorBase(baseColor.decrB())

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                (MessageChannel.COLOR_PALETTE_BRIDGE.isType(msg.message) ) -> {
                    val colorPaletteMessage : ColorPaletteMessage = MessageChannel.COLOR_PALETTE_BRIDGE.receiveMessage(msg.extraInfo)

                    baseColor = colorPaletteMessage.colorPalette
                    baseColorName = colorPaletteMessage.name

                    recreateSpectrumSwatches()
                    return true
                }
            }
        }
        return false
    }

    fun dispose() {
        sampleSwatchesCtrl.dispose()
        baseSwatchesCtrl.dispose()
        compSwatchesCtrl.dispose()
        triadFirstSwatchesCtrl.dispose()
        triadSecondSwatchesCtrl.dispose()
    }
}