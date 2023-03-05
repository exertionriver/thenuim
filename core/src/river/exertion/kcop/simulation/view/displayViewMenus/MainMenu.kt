package river.exertion.kcop.simulation.view.displayViewMenus

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import river.exertion.kcop.assets.FontSize
import river.exertion.kcop.simulation.view.ViewType
import river.exertion.kcop.system.ShapeDrawerConfig
import river.exertion.kcop.system.colorPalette.ColorPalette

class MainMenu(override var screenWidth: Float, override var screenHeight: Float) : DisplayViewMenu {

    override val tag = "mainMenu"
    override val label = "Main"
    override val sdcMap : MutableMap<Int, ShapeDrawerConfig?> = mutableMapOf()

    override val backgroundColor = ColorPalette.of("blue")

    override var menuTable = Table().apply { this.debug() }

    override fun getMenu(batch : Batch, bitmapFont: BitmapFont) : Table {

        return genericLayout(batch, bitmapFont)
    }

}