package river.exertion.kcop.sim.colorPalette.view

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import river.exertion.kcop.sim.colorPalette.view.ColorPaletteLayout.colorBaseDecr
import river.exertion.kcop.sim.colorPalette.view.ColorPaletteLayout.colorBaseDecrB
import river.exertion.kcop.sim.colorPalette.view.ColorPaletteLayout.colorBaseDecrG
import river.exertion.kcop.sim.colorPalette.view.ColorPaletteLayout.colorBaseDecrR
import river.exertion.kcop.sim.colorPalette.view.ColorPaletteLayout.colorBaseIncr
import river.exertion.kcop.sim.colorPalette.view.ColorPaletteLayout.colorBaseIncrB
import river.exertion.kcop.sim.colorPalette.view.ColorPaletteLayout.colorBaseIncrG
import river.exertion.kcop.sim.colorPalette.view.ColorPaletteLayout.colorBaseIncrR
import river.exertion.kcop.sim.colorPalette.view.ColorPaletteLayout.colorSampleNext
import river.exertion.kcop.sim.colorPalette.view.ColorPaletteLayout.colorSamplePrev

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

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean { return false }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean { return false }

    override fun scrolled(amountX: Float, amountY: Float): Boolean { return false }
}