package river.exertion.kcop.view.menu

import com.badlogic.gdx.Gdx
import river.exertion.kcop.asset.view.ColorPalette
import river.exertion.kcop.view.messaging.menuParams.ActionParam
import river.exertion.kcop.view.switchboard.ViewSwitchboard
import kotlin.system.exitProcess

object MainMenu : DisplayViewMenu {

    override val tag = "mainMenu"
    override val label = "Main"

    override val backgroundColor = ColorPalette.of("blue")

    override fun menuPane() = null

    override val breadcrumbEntries = mapOf<String, String>()

    override fun navs() = assignableNavs

    override val actions = mutableListOf(
        ActionParam("Exit kcop", {
            Gdx.app.exit()
            exitProcess(0)
        }, "Peace Out"),
        ActionParam("Close Menu", {
            ViewSwitchboard.closeMenu()
        })
    )

    val assignableNavs = mutableListOf<ActionParam>()
}