package river.exertion.thenuim.sim.narrative

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import river.exertion.thenuim.messaging.MessageChannelHandler
import river.exertion.thenuim.sim.narrative.NarrativeLoPa.NarrativeBridge
import river.exertion.thenuim.sim.narrative.messaging.NarrativeComponentMessage
import river.exertion.thenuim.view.layout.PauseView

object NarrativeInputProcessor : InputProcessor {

    override fun keyDown(keycode: Int): Boolean {
        if (!PauseView.isChecked) {
            MessageChannelHandler.send(NarrativeBridge, NarrativeComponentMessage(NarrativeComponentMessage.NarrativeMessageType.Next, Input.Keys.toString(keycode)))
        }

        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchCancelled(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        return false
    }
}