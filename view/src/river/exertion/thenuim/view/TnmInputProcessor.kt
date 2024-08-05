package river.exertion.thenuim.view

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import river.exertion.thenuim.view.layout.InputView
import river.exertion.thenuim.view.layout.ButtonView

object TnmInputProcessor : InputProcessor {

    override fun keyDown(keycode: Int): Boolean {
        InputView.keyEvent(Input.Keys.toString(keycode))
        InputView.build()

        if (keycode == Input.Keys.ESCAPE) ButtonView.closeMenu()

        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        InputView.releaseEvent()
        InputView.build()

        return false
    }

    override fun keyTyped(character: Char): Boolean {
        InputView.keyEvent(character.toString())
        InputView.build()

        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        InputView.touchEvent(screenX, screenY, button)
        InputView.build()

        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        InputView.releaseEvent()
        InputView.build()

        return false
    }

    override fun touchCancelled(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        InputView.releaseEvent()
        InputView.build()

        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        InputView.touchEvent(screenX, screenY, -1)
        InputView.build()

        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        return false
    }
}