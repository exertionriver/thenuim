package river.exertion.kcop.simulation.view.displayViewMenus

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import river.exertion.kcop.simulation.view.ViewType
import river.exertion.kcop.system.ShapeDrawerConfig
import river.exertion.kcop.system.colorPalette.ColorPalette

class MainMenu(override var screenWidth: Float, override var screenHeight: Float) : DisplayViewMenu {

    override val tag = "mainMenu"
    override val sdcMap : MutableMap<Int, ShapeDrawerConfig?> = mutableMapOf()

    override fun getMenu(batch : Batch) : Table {

        val paneColor = ColorPalette.of("blue")

        if (sdcMap[0] != null) sdcMap[0]!!.dispose()

        sdcMap[0] = ShapeDrawerConfig(batch, paneColor.color())

        return Table().apply { this.add(
            Image(TextureRegionDrawable(
                sdcMap[0]!!.textureRegion.apply {this.setRegion(0, 0,
                    ViewType.secondWidth(screenWidth).toInt() - 1,
                    ViewType.secondHeight(screenHeight).toInt() - 1)
                })
            )
        )}
    }

}