package river.exertion.thenuim.view.menu

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.thenuim.view.ViewLoPa.MainMenuBackgroundColor
import river.exertion.thenuim.view.layout.ButtonView
import kotlin.system.exitProcess

object MainMenu : DisplayViewMenu {

    override val tag = "mainMenu"
    override val label = "Main"

    override val backgroundColor = MainMenuBackgroundColor

    override var menuPane = { Table() }

    override val breadcrumbEntries = mapOf<String, String>()

    override val assignableNavs = mutableListOf<MenuActionParam>()

    override val assignableActions = mutableListOf(
        MenuActionParam("Exit Thenuim", {
            Gdx.app.exit()
            exitProcess(0)
        }, "Peace Out"),
        MenuActionParam("Close Menu", {
            ButtonView.closeMenu()
        })
    )
}