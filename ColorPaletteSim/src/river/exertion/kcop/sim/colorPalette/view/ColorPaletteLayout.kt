package river.exertion.kcop.sim.colorPalette.view

import river.exertion.kcop.view.ColorPalette
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.sim.colorPalette.messaging.ColorPaletteMessage
import river.exertion.kcop.view.layout.ViewType

class ColorPaletteLayout(var width : Float, var height : Float) : Telegraph {

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

    fun build(stage : Stage) {
        stage.addActor(createSampleSwatchesCtrl())
        stage.addActor(createBaseSwatchesCtrl())
        stage.addActor(createCompSwatchesCtrl())
        stage.addActor(createTriadFirstSwatchesCtrl())
        stage.addActor(createTriadSecondSwatchesCtrl())
    }

    fun hide() {
        sampleSwatchesCtrl.addAction(Actions.hide())
        baseSwatchesCtrl.addAction(Actions.hide())
        compSwatchesCtrl.addAction(Actions.hide())
        triadFirstSwatchesCtrl.addAction(Actions.hide())
        triadSecondSwatchesCtrl.addAction(Actions.hide())
    }

    fun show() {
        sampleSwatchesCtrl.addAction(Actions.show())
        baseSwatchesCtrl.addAction(Actions.show())
        compSwatchesCtrl.addAction(Actions.show())
        triadFirstSwatchesCtrl.addAction(Actions.show())
        triadSecondSwatchesCtrl.addAction(Actions.show())
    }

    fun kcopScreen(offset : Vector2) {
        sampleSwatchesCtrl.addAction(Actions.sequence(Actions.moveBy(-offset.x, -offset.y, .25f, Interpolation.linear)))
        baseSwatchesCtrl.addAction(Actions.sequence(Actions.moveBy(-offset.x, -offset.y, .25f, Interpolation.linear)))
        compSwatchesCtrl.addAction(Actions.sequence(Actions.moveBy(-offset.x, -offset.y, .25f, Interpolation.linear)))
        triadFirstSwatchesCtrl.addAction(Actions.sequence(Actions.moveBy(-offset.x, -offset.y, .25f, Interpolation.linear)))
        triadSecondSwatchesCtrl.addAction(Actions.sequence(Actions.moveBy(-offset.x, -offset.y, .25f, Interpolation.linear)))

        sampleSwatchesCtrl.topX -= offset.x
        sampleSwatchesCtrl.topY -= offset.y
        baseSwatchesCtrl.topX -= offset.x
        baseSwatchesCtrl.topY -= offset.y
        compSwatchesCtrl.topX -= offset.x
        compSwatchesCtrl.topY -= offset.y
        triadFirstSwatchesCtrl.topX -= offset.x
        triadFirstSwatchesCtrl.topY -= offset.y
        triadSecondSwatchesCtrl.topX -= offset.x
        triadSecondSwatchesCtrl.topY -= offset.y
    }

    fun fullScreen(offset : Vector2) {
        sampleSwatchesCtrl.addAction(Actions.sequence(Actions.moveBy(offset.x, offset.y, .25f, Interpolation.linear)))
        baseSwatchesCtrl.addAction(Actions.sequence(Actions.moveBy(offset.x, offset.y, .25f, Interpolation.linear)))
        compSwatchesCtrl.addAction(Actions.sequence(Actions.moveBy(offset.x, offset.y, .25f, Interpolation.linear)))
        triadFirstSwatchesCtrl.addAction(Actions.sequence(Actions.moveBy(offset.x, offset.y, .25f, Interpolation.linear)))
        triadSecondSwatchesCtrl.addAction(Actions.sequence(Actions.moveBy(offset.x, offset.y, .25f, Interpolation.linear)))

        sampleSwatchesCtrl.topX += offset.x
        sampleSwatchesCtrl.topY += offset.y
        baseSwatchesCtrl.topX += offset.x
        baseSwatchesCtrl.topY += offset.y
        compSwatchesCtrl.topX += offset.x
        compSwatchesCtrl.topY += offset.y
        triadFirstSwatchesCtrl.topX += offset.x
        triadFirstSwatchesCtrl.topY += offset.y
        triadSecondSwatchesCtrl.topX += offset.x
        triadSecondSwatchesCtrl.topY += offset.y
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

    fun dispose() {
        sampleSwatchesCtrl.dispose()
        baseSwatchesCtrl.dispose()
        compSwatchesCtrl.dispose()
        triadFirstSwatchesCtrl.dispose()
        triadSecondSwatchesCtrl.dispose()
    }

    companion object {
        const val ColorPaletteBridge = "ColorPaletteBridge"
    }
}