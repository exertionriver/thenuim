package river.exertion.kcop

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import river.exertion.kcop.base.ColorPaletteMessage
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.*

class ColorPaletteInputProcessor : InputProcessor {

    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.R -> { MessageChannel.COLOR_PALETTE_BRIDGE.send(null, ColorPaletteMessage(ColorPaletteMessage.ColorPaletteMessageType.ModifyBaseColor, modifyType = ColorPaletteMessage.ColorPaletteModifyType.ColorBaseIncrR)) }
            Input.Keys.G -> { MessageChannel.COLOR_PALETTE_BRIDGE.send(null, ColorPaletteMessage(ColorPaletteMessage.ColorPaletteMessageType.ModifyBaseColor, modifyType = ColorPaletteMessage.ColorPaletteModifyType.ColorBaseIncrG)) }
            Input.Keys.B -> { MessageChannel.COLOR_PALETTE_BRIDGE.send(null, ColorPaletteMessage(ColorPaletteMessage.ColorPaletteMessageType.ModifyBaseColor, modifyType = ColorPaletteMessage.ColorPaletteModifyType.ColorBaseIncrB)) }

            Input.Keys.E -> { MessageChannel.COLOR_PALETTE_BRIDGE.send(null, ColorPaletteMessage(ColorPaletteMessage.ColorPaletteMessageType.ModifyBaseColor, modifyType = ColorPaletteMessage.ColorPaletteModifyType.ColorBaseDecrR)) }
            Input.Keys.F -> { MessageChannel.COLOR_PALETTE_BRIDGE.send(null, ColorPaletteMessage(ColorPaletteMessage.ColorPaletteMessageType.ModifyBaseColor, modifyType = ColorPaletteMessage.ColorPaletteModifyType.ColorBaseDecrG)) }
            Input.Keys.V -> { MessageChannel.COLOR_PALETTE_BRIDGE.send(null, ColorPaletteMessage(ColorPaletteMessage.ColorPaletteMessageType.ModifyBaseColor, modifyType = ColorPaletteMessage.ColorPaletteModifyType.ColorBaseDecrB)) }

            Input.Keys.UP -> { MessageChannel.COLOR_PALETTE_BRIDGE.send(null, ColorPaletteMessage(ColorPaletteMessage.ColorPaletteMessageType.ModifyBaseColor, modifyType = ColorPaletteMessage.ColorPaletteModifyType.ColorBaseIncr)) }
            Input.Keys.DOWN -> { MessageChannel.COLOR_PALETTE_BRIDGE.send(null, ColorPaletteMessage(ColorPaletteMessage.ColorPaletteMessageType.ModifyBaseColor, modifyType = ColorPaletteMessage.ColorPaletteModifyType.ColorBaseDecr)) }
            Input.Keys.LEFT -> { MessageChannel.COLOR_PALETTE_BRIDGE.send(null, ColorPaletteMessage(ColorPaletteMessage.ColorPaletteMessageType.ModifyBaseColor, modifyType = ColorPaletteMessage.ColorPaletteModifyType.ColorSamplePrev)) }
            Input.Keys.RIGHT -> { MessageChannel.COLOR_PALETTE_BRIDGE.send(null, ColorPaletteMessage(ColorPaletteMessage.ColorPaletteMessageType.ModifyBaseColor, modifyType = ColorPaletteMessage.ColorPaletteModifyType.ColorSampleNext)) }
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