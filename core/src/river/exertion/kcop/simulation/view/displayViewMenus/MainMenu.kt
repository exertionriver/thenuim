package river.exertion.kcop.simulation.view.displayViewMenus

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.system.ShapeDrawerConfig
import river.exertion.kcop.system.colorPalette.ColorPalette

class MainMenu(override var screenWidth: Float, override var screenHeight: Float) : DisplayViewMenu {

    override val sdcMap : MutableMap<Int, ShapeDrawerConfig?> = mutableMapOf()

    override val backgroundColor = ColorPalette.of("blue")

    override fun menuPane(bitmapFont: BitmapFont) = null

    override val breadcrumbEntries = mapOf<String, String>()

    override val navs = mapOf(
        "Profile >" to ProfileMenuParams("profileMenu", null)
    )

    override val actions = mapOf<String, Pair<String, () -> Unit>>()

    override fun tag() = tag
    override fun label() = label

    companion object {
        const val tag = "mainMenu"
        const val label = "Main"
    }
}