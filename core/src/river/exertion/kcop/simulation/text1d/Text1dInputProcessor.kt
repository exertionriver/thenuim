package river.exertion.kcop.simulation.text1d

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import river.exertion.kcop.system.MessageChannel
import river.exertion.kcop.system.text1d.Text1dMessage

class Text1dInputProcessor : InputProcessor {
    override fun keyDown(keycode: Int): Boolean {
        MessageChannel.TEXT1D_BRIDGE.send(null, Text1dMessage(Input.Keys.toString(keycode)))
        return true
    }

    override fun keyUp(keycode: Int): Boolean {
        return true
    }

    override fun keyTyped(character: Char): Boolean {
        return true
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return true
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return true
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return true
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        return true
    }
}