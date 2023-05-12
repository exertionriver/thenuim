package river.exertion.kcop.sim.colorPalette.view

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.sim.colorPalette.ColorPalettePackage.Companion.ColorPaletteBridge
import river.exertion.kcop.sim.colorPalette.messaging.ColorPaletteMessage
import river.exertion.kcop.sim.colorPalette.view.ColorPaletteLayout

object ColorPaletteInputProcessor : InputProcessor {

    fun modifyBaseColor(colorPaletteModifyType: ColorPaletteMessage.ColorPaletteModifyType) {
        MessageChannelHandler.send(
            ColorPaletteBridge, ColorPaletteMessage(
            ColorPaletteMessage.ColorPaletteMessageType.ModifyBaseColor, modifyType = colorPaletteModifyType)
        )
    }

    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.R -> { modifyBaseColor(ColorPaletteMessage.ColorPaletteModifyType.ColorBaseIncrR) }
            Input.Keys.G -> { modifyBaseColor(ColorPaletteMessage.ColorPaletteModifyType.ColorBaseIncrG) }
            Input.Keys.B -> { modifyBaseColor(ColorPaletteMessage.ColorPaletteModifyType.ColorBaseIncrB) }

            Input.Keys.E -> { modifyBaseColor(ColorPaletteMessage.ColorPaletteModifyType.ColorBaseDecrR) }
            Input.Keys.F -> { modifyBaseColor(ColorPaletteMessage.ColorPaletteModifyType.ColorBaseDecrG) }
            Input.Keys.V -> { modifyBaseColor(ColorPaletteMessage.ColorPaletteModifyType.ColorBaseDecrB) }

            Input.Keys.UP -> { modifyBaseColor(ColorPaletteMessage.ColorPaletteModifyType.ColorBaseIncr) }
            Input.Keys.DOWN -> { modifyBaseColor(ColorPaletteMessage.ColorPaletteModifyType.ColorBaseDecr) }
            Input.Keys.LEFT -> { modifyBaseColor(ColorPaletteMessage.ColorPaletteModifyType.ColorSamplePrev) }
            Input.Keys.RIGHT -> { modifyBaseColor(ColorPaletteMessage.ColorPaletteModifyType.ColorSampleNext) }
        }

        return false
    }

    override fun keyUp(keycode: Int): Boolean { return false }

    override fun keyTyped(character: Char): Boolean { return false }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean { return false }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean { return false }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean { return false }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean { return false }

    override fun scrolled(amountX: Float, amountY: Float): Boolean { return false }
}