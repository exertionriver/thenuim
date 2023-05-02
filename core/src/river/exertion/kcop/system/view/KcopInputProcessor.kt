package river.exertion.kcop.system.view

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import river.exertion.kcop.system.messaging.MessageChannelEnum
import river.exertion.kcop.system.messaging.Switchboard
import river.exertion.kcop.system.messaging.messages.*
import river.exertion.kcop.view.messaging.InputViewMessage

class KcopInputProcessor : InputProcessor {

    override fun keyDown(keycode: Int): Boolean {
        MessageChannelEnum.NARRATIVE_BRIDGE_PAUSE_GATE.send(null, NarrativeMessage(NarrativeMessage.NarrativeMessageType.Next, Input.Keys.toString(keycode)))

        MessageChannelEnum.INPUT_VIEW_BRIDGE.send(null, InputViewMessage(
            event = InputViewMessage.InputViewMessageEvent.KeyDownEvent,
            eventParams = mapOf(InputViewMessage.InputViewMessageParam.KeycodeStrKey to Input.Keys.toString(keycode)))
        )

        if (keycode == Input.Keys.ESCAPE) Switchboard.closeMenu()

        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        MessageChannelEnum.INPUT_VIEW_BRIDGE.send(null, InputViewMessage(
            event = InputViewMessage.InputViewMessageEvent.KeyUpEvent,
            eventParams = mapOf(InputViewMessage.InputViewMessageParam.KeycodeStrKey to Input.Keys.toString(keycode)))
        )
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        MessageChannelEnum.INPUT_VIEW_BRIDGE.send(null, InputViewMessage(
            event = InputViewMessage.InputViewMessageEvent.KeyTypedEvent,
            eventParams = mapOf(InputViewMessage.InputViewMessageParam.CharacterKey to character))
        )
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        MessageChannelEnum.INPUT_VIEW_BRIDGE.send(null, InputViewMessage(
            event = InputViewMessage.InputViewMessageEvent.TouchDownEvent,
            eventParams = mapOf(
                InputViewMessage.InputViewMessageParam.ScreenXKey to screenX,
                InputViewMessage.InputViewMessageParam.ScreenYKey to screenY,
                InputViewMessage.InputViewMessageParam.PointerKey to pointer,
                InputViewMessage.InputViewMessageParam.ButtonKey to button)
            )
        )
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        MessageChannelEnum.INPUT_VIEW_BRIDGE.send(null, InputViewMessage(
            event = InputViewMessage.InputViewMessageEvent.TouchUpEvent,
            eventParams = mapOf(
                InputViewMessage.InputViewMessageParam.ScreenXKey to screenX,
                InputViewMessage.InputViewMessageParam.ScreenYKey to screenY,
                InputViewMessage.InputViewMessageParam.PointerKey to pointer,
                InputViewMessage.InputViewMessageParam.ButtonKey to button)
            )
        )
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        MessageChannelEnum.INPUT_VIEW_BRIDGE.send(null, InputViewMessage(
            event = InputViewMessage.InputViewMessageEvent.TouchDraggedEvent,
            eventParams = mapOf(
                InputViewMessage.InputViewMessageParam.ScreenXKey to screenX,
                InputViewMessage.InputViewMessageParam.ScreenYKey to screenY,
                InputViewMessage.InputViewMessageParam.PointerKey to pointer)
            )
        )
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        MessageChannelEnum.INPUT_VIEW_BRIDGE.send(null, InputViewMessage(
            event = InputViewMessage.InputViewMessageEvent.MouseMovedEvent,
            eventParams = mapOf(
                InputViewMessage.InputViewMessageParam.ScreenXKey to screenX,
                InputViewMessage.InputViewMessageParam.ScreenYKey to screenY)
            )
        )
        return false
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        MessageChannelEnum.INPUT_VIEW_BRIDGE.send(null, InputViewMessage(
            event = InputViewMessage.InputViewMessageEvent.ScrolledEvent,
            eventParams = mapOf(
                InputViewMessage.InputViewMessageParam.AmountXKey to amountX,
                InputViewMessage.InputViewMessageParam.AmountYKey to amountY)
            )
        )
        return false
    }
}