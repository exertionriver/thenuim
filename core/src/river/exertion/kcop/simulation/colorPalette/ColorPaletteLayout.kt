package river.exertion.kcop.simulation.colorPalette

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Actor
import river.exertion.kcop.system.MessageChannel
import river.exertion.kcop.system.colorPalette.ColorPalette
import river.exertion.kcop.system.colorPalette.ColorPaletteMessage
import river.exertion.kcop.system.layout.LayoutView
import kotlin.reflect.jvm.javaMethod

class ColorPaletteLayout(var width : Float, var height : Float) : Telegraph {

    var bitmapFont : BitmapFont? = null
    var batch : Batch? = null

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

    init {
        MessageChannel.COLOR_PALETTE_BRIDGE.enableReceive(this)
    }

    fun colorColumnWidth() = LayoutView.fifthWidth(width) + LayoutView.seventhWidth(width) / 2

    fun firstColorColumn() = LayoutView.seventhWidth(width)
    fun secondColorColumn() = firstColorColumn() + colorColumnWidth()
    fun thirdColorColumn() = secondColorColumn() + colorColumnWidth()
    fun fourthColorColumn() = thirdColorColumn() + colorColumnWidth()
    fun fifthColorColumn() = fourthColorColumn() + colorColumnWidth()

    fun colorRowHeight() = LayoutView.seventhHeight(height)

    fun firstColorRow() = LayoutView.firstHeight(height) - colorRowHeight()

    fun colorSwatchHeight() = colorRowHeight()
    fun colorSwatchWidth() = colorColumnWidth()

    private fun createSampleSwatches() {
        if (bitmapFont == null) throw Exception("${::createSampleSwatches.javaMethod?.name}: bitmapFont needs to be set")
        if (batch == null) throw Exception("${::createSampleSwatches.javaMethod?.name}: batch needs to be set")

        if (sampleSwatchesCtrl.bitmapFont == null) sampleSwatchesCtrl.bitmapFont = bitmapFont
        if (sampleSwatchesCtrl.batch == null) sampleSwatchesCtrl.batch = batch

        sampleSwatchesCtrl.swatchEntries = ColorPalette.w3cColors()[sampleSwatchesIdx]
        sampleSwatchesCtrl.create()
    }

    fun createSampleSwatchesCtrl() : Actor {
        createSampleSwatches()

        return sampleSwatchesCtrl
    }

    fun colorSamplePrev() {
        if (sampleSwatchesIdx == sampleSwatchesMinIdx) sampleSwatchesIdx = sampleSwatchesMaxIdx else sampleSwatchesIdx--

        createSampleSwatches()
    }

    fun colorSampleNext() {
        if (sampleSwatchesIdx == sampleSwatchesMaxIdx) sampleSwatchesIdx = sampleSwatchesMinIdx else sampleSwatchesIdx++

        createSampleSwatches()
    }

    private fun createSpectrumSwatches(swatches : ColorSwatchesCtrl, colorPalette : ColorPalette, colorNameOverride : String? = null)  {
        if (bitmapFont == null) throw Exception("${::createSpectrumSwatches.javaMethod?.name}: bitmapFont needs to be set")
        if (batch == null) throw Exception("${::createSpectrumSwatches.javaMethod?.name}: batch needs to be set")

        if (swatches.bitmapFont == null) swatches.bitmapFont = bitmapFont
        if (swatches.batch == null) swatches.batch = batch

        val colorName = colorNameOverride ?: colorPalette.tags()[0]

        swatches.swatchEntries = colorPalette.labelledSpectrum(colorName)
        swatches.createWithHeader(colorName, colorPalette)
    }

    private fun recreateSpectrumSwatches() {
        createBaseSwatches()
        createCompSwatches()
        createTriadFirstSwatches()
        createTriadSecondSwatches()
    }

    private fun createBaseSwatches() = createSpectrumSwatches(baseSwatchesCtrl, baseColor, baseColorName)
    private fun createCompSwatches() = createSpectrumSwatches(compSwatchesCtrl, baseColor.comp())
    private fun createTriadFirstSwatches() = createSpectrumSwatches(triadFirstSwatchesCtrl, baseColor.triad().first)
    private fun createTriadSecondSwatches() = createSpectrumSwatches(triadSecondSwatchesCtrl, baseColor.triad().second)

    fun createBaseSwatchesCtrl() : Actor {
        createBaseSwatches()
        return baseSwatchesCtrl
    }

    fun createCompSwatchesCtrl() : Actor {
        createCompSwatches()
        return compSwatchesCtrl
    }

    fun createTriadFirstSwatchesCtrl() : Actor {
        createTriadFirstSwatches()
        return triadFirstSwatchesCtrl
    }

    fun createTriadSecondSwatchesCtrl() : Actor {
        createTriadSecondSwatches()
        return triadSecondSwatchesCtrl
    }

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
            val colorPaletteMessage : ColorPaletteMessage = MessageChannel.COLOR_PALETTE_BRIDGE.receiveMessage(msg.extraInfo)

            baseColor = colorPaletteMessage.colorPalette
            baseColorName = colorPaletteMessage.name

            recreateSpectrumSwatches()
        }
        return true
    }
}