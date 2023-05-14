package river.exertion.kcop.view.menu

import com.badlogic.gdx.Gdx
import river.exertion.kcop.asset.view.ColorPalette
import river.exertion.kcop.view.layout.MenuView
import kotlin.system.exitProcess

object MainMenu : DisplayViewMenu {

    override val tag = "mainMenu"
    override val label = "Main"

    override val backgroundColor = ColorPalette.of("blue")

    override fun menuPane() = null

    override val breadcrumbEntries = mapOf<String, String>()

    override val assignableNavs = mutableListOf<MenuActionParam>()

    override val actions = mutableListOf(
        MenuActionParam("Exit kcop", {
            Gdx.app.exit()
            exitProcess(0)
        }, "Peace Out"),
        MenuActionParam("Close Menu", {
            MenuView.closeMenu()
        })
    )
}