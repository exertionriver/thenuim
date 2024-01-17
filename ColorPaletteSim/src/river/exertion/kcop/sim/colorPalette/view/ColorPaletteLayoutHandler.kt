package river.exertion.kcop.sim.colorPalette.view

import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.asset.view.ColorPalette
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.klop.IDisplayViewLayoutHandler
import river.exertion.kcop.view.layout.DisplayView
import river.exertion.kcop.view.layout.ViewType
import river.exertion.kcop.view.layout.displayViewLayout.DVLayoutHandler

object ColorPaletteLayoutHandler : IDisplayViewLayoutHandler {

    var baseColorName = "darkGray"
    var baseColor = ColorPalette.of(baseColorName)

    var sampleSwatchesIdx = 0
    var sampleSwatchesMinIdx = 0
    var sampleSwatchesMaxIdx = ColorPalette.w3cColors().size - 1

    var sampleSwatchesCtrl = ColorSwatchBase(firstColorColumn(), firstColorRow(), colorSwatchWidth(), colorSwatchHeight())
    var baseSwatchesCtrl = ColorSwatchBase(secondColorColumn(), firstColorRow(), colorSwatchWidth(), colorSwatchHeight())
    var compSwatchesCtrl = ColorSwatchBase(thirdColorColumn(), firstColorRow(), colorSwatchWidth(), colorSwatchHeight())
    var triadFirstSwatchesCtrl = ColorSwatchBase(fourthColorColumn(), firstColorRow(), colorSwatchWidth(), colorSwatchHeight())
    var triadSecondSwatchesCtrl = ColorSwatchBase(fifthColorColumn(), firstColorRow(), colorSwatchWidth(), colorSwatchHeight())

    fun colorColumnWidth() = ViewType.fifthWidth(KcopSkin.screenWidth) + 2 * ViewType.padWidth(KcopSkin.screenWidth)

    fun firstColorColumn() = ViewType.seventhWidth(KcopSkin.screenWidth)
    fun secondColorColumn() = firstColorColumn() + colorColumnWidth()
    fun thirdColorColumn() = secondColorColumn() + colorColumnWidth()
    fun fourthColorColumn() = thirdColorColumn() + colorColumnWidth()
    fun fifthColorColumn() = fourthColorColumn() + colorColumnWidth()

    fun colorRowHeight() = ViewType.seventhHeight(KcopSkin.screenHeight)

    fun firstColorRow() = ViewType.firstHeight(KcopSkin.screenHeight) - colorRowHeight()

    fun colorSwatchHeight() = colorRowHeight()
    fun colorSwatchWidth() = colorColumnWidth()

    fun createSampleSwatchesCtrl(): Table {
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

    private fun createSpectrumSwatchesCtrl(
        swatchesCtrl: ColorSwatchBase,
        colorPalette: ColorPalette,
        colorNameOverride: String? = null
    ): Table {
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

    override fun build()  {
        DisplayView.displayViewTable = Table().apply {
            this.add(createSampleSwatchesCtrl()).top()
            this.add(createBaseSwatchesCtrl()).top()
            this.add(createCompSwatchesCtrl()).top()
            this.add(createTriadFirstSwatchesCtrl()).top()
            this.add(createTriadSecondSwatchesCtrl()).top()
            this.row()
            this.add(Table()).colspan(5).grow()
        }

        DVLayoutHandler.build()
    }

    override fun clearContent() {
        sampleSwatchesCtrl.clearChildren()
        baseSwatchesCtrl.clearChildren()
        compSwatchesCtrl.clearChildren()
        triadFirstSwatchesCtrl.clearChildren()
        triadSecondSwatchesCtrl.clearChildren()

        DisplayView.displayViewTable.clearChildren()

        DVLayoutHandler.clearContent()
    }

    fun setBaseColor(baseColorName: String?, baseColorPalette: ColorPalette?) {
        this.baseColor = baseColorPalette ?: baseColor
        this.baseColorName = baseColorName ?: baseColor.tags()[0]

        recreateSpectrumSwatches()
    }

}