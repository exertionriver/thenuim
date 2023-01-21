package river.exertion.kcop

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Actor
import kotlin.reflect.jvm.javaMethod

class ColorPaletteLayout(override var width : Float, override var height : Float) : Layout(width, height), Telegraph {

    var baseColorName = "darkGray"
    var baseColor = ColorPalette.of(baseColorName)

    var bitmapFont : BitmapFont? = null
    var batch : Batch? = null

    var sampleSwatchesIdx = 0
    var sampleSwatchesMinIdx = 0
    var sampleSwatchesMaxIdx = ColorPalette.w3cColors().size - 1

    var sampleSwatches = ColorSwatches(firstColorColumn(), firstColorRow(), colorSwatchWidth(), colorSwatchHeight())
    var baseSwatches = ColorSwatches(secondColorColumn(), firstColorRow(), colorSwatchWidth(), colorSwatchHeight())
    var compSwatches = ColorSwatches(thirdColorColumn(), firstColorRow(), colorSwatchWidth(), colorSwatchHeight())
    var triadFirstSwatches = ColorSwatches(fourthColorColumn(), firstColorRow(), colorSwatchWidth(), colorSwatchHeight())
    var triadSecondSwatches = ColorSwatches(fifthColorColumn(), firstColorRow(), colorSwatchWidth(), colorSwatchHeight())

    init {
        MessageChannel.COLOR_PALETTE_BRIDGE.enableReceive(this)
    }

    fun colorColumnWidth() = fifthWidth() + seventhWidth() / 2

    fun firstColorColumn() = seventhWidth()
    fun secondColorColumn() = firstColorColumn() + colorColumnWidth()
    fun thirdColorColumn() = secondColorColumn() + colorColumnWidth()
    fun fourthColorColumn() = thirdColorColumn() + colorColumnWidth()
    fun fifthColorColumn() = fourthColorColumn() + colorColumnWidth()

    fun firstTextColumn() = firstColorColumn() + seventhWidth() / 2
    fun secondTextColumn() = firstTextColumn() + colorColumnWidth()
    fun thirdTextColumn() = secondTextColumn() + colorColumnWidth()
    fun fourthTextColumn() = thirdTextColumn() + colorColumnWidth()
    fun fifthTextColumn() = fourthTextColumn() + colorColumnWidth()

    fun colorRowHeight() = seventhHeight()

    fun firstColorRow() = firstHeight() - colorRowHeight()
    fun firstTextRow() = firstHeight() - colorRowHeight() / 2

    fun colorSwatchHeight() = colorRowHeight()
    fun colorSwatchWidth() = colorColumnWidth()

    private fun createSampleSwatches() {
        if (bitmapFont == null) throw Exception("${::createSampleSwatches.javaMethod?.name}: bitmapFont needs to be set")
        if (batch == null) throw Exception("${::createSampleSwatches.javaMethod?.name}: batch needs to be set")

        if (sampleSwatches.bitmapFont == null) sampleSwatches.bitmapFont = bitmapFont
        if (sampleSwatches.batch == null) sampleSwatches.batch = batch

        sampleSwatches.swatchEntries = ColorPalette.w3cColors()[sampleSwatchesIdx]
        sampleSwatches.create()
    }

    fun createSampleSwatchesCtrl() : Actor {
        createSampleSwatches()

        return sampleSwatches.table
    }

    fun colorSamplePrev() {
        if (sampleSwatchesIdx == sampleSwatchesMinIdx) sampleSwatchesIdx = sampleSwatchesMaxIdx else sampleSwatchesIdx--

        createSampleSwatches()
    }

    fun colorSampleNext() {
        if (sampleSwatchesIdx == sampleSwatchesMaxIdx) sampleSwatchesIdx = sampleSwatchesMinIdx else sampleSwatchesIdx++

        createSampleSwatches()
    }

    private fun createSpectrumSwatches(swatches : ColorSwatches, colorPalette : ColorPalette, colorNameOverride : String? = null)  {
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

    private fun createBaseSwatches() = createSpectrumSwatches(baseSwatches, baseColor, baseColorName)
    private fun createCompSwatches() = createSpectrumSwatches(compSwatches, baseColor.comp())
    private fun createTriadFirstSwatches() = createSpectrumSwatches(triadFirstSwatches, baseColor.triad().first)
    private fun createTriadSecondSwatches() = createSpectrumSwatches(triadSecondSwatches, baseColor.triad().second)

    fun createBaseSwatchesCtrl() : Actor {
        createBaseSwatches()
        return baseSwatches.table
    }

    fun createCompSwatchesCtrl() : Actor {
        createCompSwatches()
        return compSwatches.table
    }

    fun createTriadFirstSwatchesCtrl() : Actor {
        createTriadFirstSwatches()
        return triadFirstSwatches.table
    }

    fun createTriadSecondSwatchesCtrl() : Actor {
        createTriadSecondSwatches()
        return triadSecondSwatches.table
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