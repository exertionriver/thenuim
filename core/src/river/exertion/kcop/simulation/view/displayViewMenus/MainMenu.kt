package river.exertion.kcop.simulation.view.displayViewMenus

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import river.exertion.kcop.simulation.view.displayViewMenus.params.ProfileMenuParams
import river.exertion.kcop.system.view.ShapeDrawerConfig
import river.exertion.kcop.system.colorPalette.ColorPalette
import kotlin.system.exitProcess

class MainMenu(override var screenWidth: Float, override var screenHeight: Float) : DisplayViewMenu {

    override val sdcMap : MutableMap<Int, ShapeDrawerConfig?> = mutableMapOf()

    override val backgroundColor = ColorPalette.of("blue")

    override fun menuPane(bitmapFont: BitmapFont) = null

    override val breadcrumbEntries = mapOf<String, String>()

    override val navs = mapOf(
        "Profile >" to ProfileMenuParams("profileMenu", null)
    )

    override val actions = mapOf<String, Pair<String, () -> Unit>>(
        "Exit" to Pair("Peace Out") {
            Gdx.app.exit()
            exitProcess(0)
        },
    )

    override fun tag() = tag
    override fun label() = label

    companion object {
        const val tag = "mainMenu"
        const val label = "Main"
    }
}