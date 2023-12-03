package river.exertion.kcop.sim.colorPalette.view

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import river.exertion.kcop.sim.colorPalette.view.ColorPaletteLayoutHandler.colorBaseDecr
import river.exertion.kcop.sim.colorPalette.view.ColorPaletteLayoutHandler.colorBaseDecrB
import river.exertion.kcop.sim.colorPalette.view.ColorPaletteLayoutHandler.colorBaseDecrG
import river.exertion.kcop.sim.colorPalette.view.ColorPaletteLayoutHandler.colorBaseDecrR
import river.exertion.kcop.sim.colorPalette.view.ColorPaletteLayoutHandler.colorBaseIncr
import river.exertion.kcop.sim.colorPalette.view.ColorPaletteLayoutHandler.colorBaseIncrB
import river.exertion.kcop.sim.colorPalette.view.ColorPaletteLayoutHandler.colorBaseIncrG
import river.exertion.kcop.sim.colorPalette.view.ColorPaletteLayoutHandler.colorBaseIncrR
import river.exertion.kcop.sim.colorPalette.view.ColorPaletteLayoutHandler.colorSampleNext
import river.exertion.kcop.sim.colorPalette.view.ColorPaletteLayoutHandler.colorSamplePrev

object ColorPaletteInputProcessor : InputProcessor {

    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.R -> { colorBaseIncrR() }
            Input.Keys.G -> { colorBaseIncrG() }
            Input.Keys.B -> { colorBaseIncrB() }

            Input.Keys.E -> { colorBaseDecrR() }
            Input.Keys.F -> { colorBaseDecrG() }
            Input.Keys.V -> { colorBaseDecrB() }

            Input.Keys.UP -> { colorBaseIncr() }
            Input.Keys.DOWN -> { colorBaseDecr() }
            Input.Keys.LEFT -> { colorSamplePrev() }
            Input.Keys.RIGHT -> { colorSampleNext() }
        }
        return false
    }

    override fun keyUp(keycode: Int): Boolean { return false }

    override fun keyTyped(character: Char): Boolean { return false }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean { return false }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean { return false }

    override fun touchCancelled(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean { return false }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean { return false }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean { return false }

    override fun scrolled(amountX: Float, amountY: Float): Boolean { return false }
}