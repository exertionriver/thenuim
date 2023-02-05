package river.exertion.kcop.system.view

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import river.exertion.kcop.system.MessageChannel
import river.exertion.kcop.system.text1d.Text1dMessage
import kotlin.reflect.jvm.javaMethod

class ViewInputProcessor : InputProcessor {

    override fun keyDown(keycode: Int): Boolean {
//        MessageChannel.TEXT1D_BRIDGE.send(null, Text1dMessage(Input.Keys.toString(keycode)))
        MessageChannel.INPUT_VIEW_BRIDGE.send(null, InputViewMessage(
            event = InputViewMessage.KeyDownEvent,
            eventParams = mapOf(InputViewMessage.KeycodeStrKey to Input.Keys.toString(keycode)))
        )
        return true
    }

    override fun keyUp(keycode: Int): Boolean {
        MessageChannel.INPUT_VIEW_BRIDGE.send(null, InputViewMessage(
            event = InputViewMessage.KeyUpEvent,
            eventParams = mapOf(InputViewMessage.KeycodeStrKey to Input.Keys.toString(keycode)))
        )
        return true
    }

    override fun keyTyped(character: Char): Boolean {
        MessageChannel.INPUT_VIEW_BRIDGE.send(null, InputViewMessage(
            event = InputViewMessage.KeyTypedEvent,
            eventParams = mapOf(InputViewMessage.CharacterKey to character))
        )
        return true
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        MessageChannel.INPUT_VIEW_BRIDGE.send(null, InputViewMessage(
            event = InputViewMessage.TouchDownEvent,
            eventParams = mapOf(
                InputViewMessage.ScreenXKey to screenX,
                InputViewMessage.ScreenYKey to screenY,
                InputViewMessage.PointerKey to pointer,
                InputViewMessage.ButtonKey to button)
            )
        )
        return true
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        MessageChannel.INPUT_VIEW_BRIDGE.send(null, InputViewMessage(
            event = InputViewMessage.TouchUpEvent,
            eventParams = mapOf(
                InputViewMessage.ScreenXKey to screenX,
                InputViewMessage.ScreenYKey to screenY,
                InputViewMessage.PointerKey to pointer,
                InputViewMessage.ButtonKey to button)
            )
        )
        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        MessageChannel.INPUT_VIEW_BRIDGE.send(null, InputViewMessage(
            event = InputViewMessage.TouchDraggedEvent,
            eventParams = mapOf(
                InputViewMessage.ScreenXKey to screenX,
                InputViewMessage.ScreenYKey to screenY,
                InputViewMessage.PointerKey to pointer)
            )
        )
        return true
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        MessageChannel.INPUT_VIEW_BRIDGE.send(null, InputViewMessage(
            event = InputViewMessage.MouseMovedEvent,
            eventParams = mapOf(
                InputViewMessage.ScreenXKey to screenX,
                InputViewMessage.ScreenYKey to screenY)
            )
        )
        return true
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        MessageChannel.INPUT_VIEW_BRIDGE.send(null, InputViewMessage(
            event = InputViewMessage.ScrolledEvent,
            eventParams = mapOf(
                InputViewMessage.AmountXKey to amountX,
                InputViewMessage.AmountYKey to amountY)
            )
        )
        return true
    }
}