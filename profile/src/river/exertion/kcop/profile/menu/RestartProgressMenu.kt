package river.exertion.kcop.profile.menu

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.asset.view.ColorPalette
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.ViewPackage
import river.exertion.kcop.view.layout.MenuView
import river.exertion.kcop.view.menu.DisplayViewMenu
import river.exertion.kcop.view.menu.DisplayViewMenuHandler
import river.exertion.kcop.view.menu.MainMenu
import river.exertion.kcop.view.menu.MenuActionParam

object RestartProgressMenu : DisplayViewMenu {

    override val tag = "restartProgressMenu"
    override val label = "Restart Progress"

    override val backgroundColor = ColorPalette.of("olive")

    var progressAssetsInfo: List<String>? = null

    override fun menuPane() = Table().apply {

        progressAssetsInfo!!.forEach { profileEntry ->
            this.add(Label(profileEntry, KcopSkin.skin).apply {
                this.wrap = true
            }).colspan(2).growX()
            this.row()
        }

        this.top()
    }

    override val breadcrumbEntries = mapOf(
//        NarrativeMenu.tag to NarrativeMenu.label,
        MainMenu.tag to MainMenu.label
    )

    override val assignableNavs = mutableListOf<MenuActionParam>()

    override val actions = mutableListOf(
        MenuActionParam("Restart", {
            MenuView.closeMenu()
        }, "Narrative Restarted!"),
        MenuActionParam("Cancel", {
            DisplayViewMenuHandler.currentMenuTag = breadcrumbEntries.keys.toList()[0]
        })
    )

}