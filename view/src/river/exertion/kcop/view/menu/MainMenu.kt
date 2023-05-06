package river.exertion.kcop.view.menu

import com.badlogic.gdx.Gdx
import river.exertion.kcop.view.ColorPalette
import river.exertion.kcop.view.messaging.menuParams.ActionParam
import river.exertion.kcop.view.switchboard.MenuViewSwitchboard
import kotlin.system.exitProcess

class MainMenu : DisplayViewMenu {

    override val backgroundColor = ColorPalette.of("blue")

    override fun menuPane() = null

    override val breadcrumbEntries = mapOf<String, String>()

    override val navs = assignableNavs

    override val actions = mutableListOf(
        ActionParam("Exit kcop", {
            Gdx.app.exit()
            exitProcess(0)
        }, "Peace Out"),
        ActionParam("Close Menu", {
            MenuViewSwitchboard.closeMenu()
        })
    )

    override fun tag() = tag
    override fun label() = label

    companion object {
        const val tag = "mainMenu"
        const val label = "Main"

        val assignableNavs = mutableListOf<ActionParam>()
    }
}