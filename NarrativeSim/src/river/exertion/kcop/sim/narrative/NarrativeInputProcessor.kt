package river.exertion.kcop.sim.narrative

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.sim.narrative.NarrativePackage.NarrativeBridge
import river.exertion.kcop.view.layout.PauseView

object NarrativeInputProcessor : InputProcessor {

    override fun keyDown(keycode: Int): Boolean {
        if (!PauseView.isChecked) {
            MessageChannelHandler.send(NarrativeBridge, Input.Keys.toString(keycode))
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