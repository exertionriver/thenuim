package river.exertion.kcop.simulation.view

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import river.exertion.kcop.simulation.view.displayViewLayouts.DVLGoldenRatio
import river.exertion.kcop.simulation.view.displayViewLayouts.DisplayViewLayout

class DisplayViewCtrl(screenWidth: Float = 50f, screenHeight: Float = 50f) : ViewCtrl(ViewType.DISPLAY, screenWidth, screenHeight) {

    var imageIsFading = false

    var displayViewLayouts : MutableList<DisplayViewLayout> = mutableListOf(
        DVLGoldenRatio(screenWidth, screenHeight)
    )

    override fun build(bitmapFont: BitmapFont, batch: Batch) {

        this.add(displayViewLayouts[0].buildPaneTable(bitmapFont, batch)).grow()

    }

    override fun dispose() {
        displayViewLayouts.forEach { it.dispose() }
    }
}