package river.exertion.kcop.sim.colorPalette.view

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.asset.view.ColorPalette
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.plugin.IDisplayViewLayoutHandler
import river.exertion.kcop.sim.colorPalette.ColorPalettePackage.ColorPaletteBridge
import river.exertion.kcop.sim.colorPalette.messaging.ColorPaletteMessage
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.layout.DisplayView
import river.exertion.kcop.view.layout.ViewType

object ColorPaletteLayout : Telegraph, IDisplayViewLayoutHandler {

    init {
        MessageChannelHandler.enableReceive(ColorPaletteBridge, this)
    }

    var baseColorName = "darkGray"
    var baseColor = ColorPalette.of(baseColorName)

    var sampleSwatchesIdx = 0
    var sampleSwatchesMinIdx = 0
    var sampleSwatchesMaxIdx = ColorPalette.w3cColors().size - 1

    var sampleSwatchesCtrl = ColorSwatchesDisplayView(firstColorColumn(), firstColorRow(), colorSwatchWidth(), colorSwatchHeight())
    var baseSwatchesCtrl = ColorSwatchesDisplayView(secondColorColumn(), firstColorRow(), colorSwatchWidth(), colorSwatchHeight())
    var compSwatchesCtrl = ColorSwatchesDisplayView(thirdColorColumn(), firstColorRow(), colorSwatchWidth(), colorSwatchHeight())
    var triadFirstSwatchesCtrl = ColorSwatchesDisplayView(fourthColorColumn(), firstColorRow(), colorSwatchWidth(), colorSwatchHeight())
    var triadSecondSwatchesCtrl = ColorSwatchesDisplayView(fifthColorColumn(), firstColorRow(), colorSwatchWidth(), colorSwatchHeight())

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

    private fun createSpectrumSwatchesCtrl(swatchesCtrl : ColorSwatchesDisplayView, colorPalette : ColorPalette, colorNameOverride : String? = null) : Table {
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

    override fun build() : Actor {
        return Table().apply {
            this.add(createSampleSwatchesCtrl()).top()
            this.add(createBaseSwatchesCtrl()).top()
            this.add(createCompSwatchesCtrl()).top()
            this.add(createTriadFirstSwatchesCtrl()).top()
            this.add(createTriadSecondSwatchesCtrl()).top()
            this.row()
            this.add(Table()).colspan(5).grow()
        }
    }

    override fun clearContent() {
        sampleSwatchesCtrl.clearChildren()
        baseSwatchesCtrl.clearChildren()
        compSwatchesCtrl.clearChildren()
        triadFirstSwatchesCtrl.clearChildren()
        triadSecondSwatchesCtrl.clearChildren()

        DisplayView.currentDisplayViewLayoutHandler = null
        DisplayView.build()
    }

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                (MessageChannelHandler.isType(ColorPaletteBridge, msg.message) ) -> {
                    val colorPaletteMessage : ColorPaletteMessage = MessageChannelHandler.receiveMessage(
                        ColorPaletteBridge, msg.extraInfo)

                    when (colorPaletteMessage.colorPaletteMessageType) {
                        ColorPaletteMessage.ColorPaletteMessageType.SetBaseColor -> {
                            baseColor = colorPaletteMessage.baseCp ?: baseColor
                            baseColorName = colorPaletteMessage.baseColorName ?: baseColor.tags()[0]

                            recreateSpectrumSwatches()
                        }
                        ColorPaletteMessage.ColorPaletteMessageType.ModifyBaseColor -> {
                            when (colorPaletteMessage.modifyType) {
                                ColorPaletteMessage.ColorPaletteModifyType.ColorBaseIncrR -> colorBaseIncrR()
                                ColorPaletteMessage.ColorPaletteModifyType.ColorBaseIncrG -> colorBaseIncrG()
                                ColorPaletteMessage.ColorPaletteModifyType.ColorBaseIncrB -> colorBaseIncrB()
                                ColorPaletteMessage.ColorPaletteModifyType.ColorBaseDecrR -> colorBaseDecrR()
                                ColorPaletteMessage.ColorPaletteModifyType.ColorBaseDecrG -> colorBaseDecrG()
                                ColorPaletteMessage.ColorPaletteModifyType.ColorBaseDecrB -> colorBaseDecrB()
                                ColorPaletteMessage.ColorPaletteModifyType.ColorBaseIncr -> colorBaseIncr()
                                ColorPaletteMessage.ColorPaletteModifyType.ColorBaseDecr -> colorBaseDecr()
                                ColorPaletteMessage.ColorPaletteModifyType.ColorSamplePrev -> colorSamplePrev()
                                ColorPaletteMessage.ColorPaletteModifyType.ColorSampleNext -> colorSampleNext()
                                else -> {}
                            }
                        }

                    }
                    return true
                }
            }
        }
        return false
    }
}