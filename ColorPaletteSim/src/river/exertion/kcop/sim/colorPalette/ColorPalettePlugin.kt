package river.exertion.kcop.sim.colorPalette

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import river.exertion.kcop.plugin.IDisplayPlugin
import river.exertion.kcop.sim.colorPalette.view.ColorPaletteLayout

class ColorPalettePlugin : IDisplayPlugin {

    lateinit var colorPaletteLayout : ColorPaletteLayout

    override fun build(screenWidth : Float, screenHeight : Float, stage : Stage) {
        colorPaletteLayout = ColorPaletteLayout(screenWidth, screenHeight)
        colorPaletteLayout.run { this.build(stage) }
    }
    override fun displayKcopScreen(offset : Vector2) {
        colorPaletteLayout.kcopScreen(offset)
    }
    override fun displayFullScreen(offset : Vector2) {
        colorPaletteLayout.fullScreen(offset)
    }

}