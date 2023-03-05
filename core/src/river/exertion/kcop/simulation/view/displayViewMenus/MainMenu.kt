package river.exertion.kcop.simulation.view.displayViewMenus

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.system.ShapeDrawerConfig
import river.exertion.kcop.system.colorPalette.ColorPalette

class MainMenu(override var screenWidth: Float, override var screenHeight: Float) : DisplayViewMenu {

    override val sdcMap : MutableMap<Int, ShapeDrawerConfig?> = mutableMapOf()

    override val backgroundColor = ColorPalette.of("blue")

    override var menuTable = Table().apply { this.debug() }

    override val breadcrumbEntries = mapOf<String, String>()

    override fun getMenu(batch : Batch, bitmapFont: BitmapFont) : Table {

        return genericLayout(batch, bitmapFont)
    }

    override fun tag() = tag
    override fun label() = label

    companion object {
        const val tag = "mainMenu"
        const val label = "Main"
    }
}