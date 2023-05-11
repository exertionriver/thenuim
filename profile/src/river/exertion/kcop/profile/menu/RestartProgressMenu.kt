package river.exertion.kcop.profile.menu

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.view.ColorPalette
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.ViewPackage
import river.exertion.kcop.view.ViewPackage.MenuViewBridge
import river.exertion.kcop.view.menu.DisplayViewMenu
import river.exertion.kcop.view.menu.DisplayViewMenuHandler
import river.exertion.kcop.view.menu.MainMenu
import river.exertion.kcop.view.messaging.DisplayViewMessage
import river.exertion.kcop.view.messaging.MenuViewMessage
import river.exertion.kcop.view.messaging.menuParams.ActionParam
import river.exertion.kcop.view.switchboard.ViewSwitchboard

object RestartProgressMenu : DisplayViewMenu {

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

    override fun navs() = mutableListOf<ActionParam>()

    override val actions = mutableListOf(
        ActionParam("Restart", {
            ViewSwitchboard.closeMenu()
        }, "Narrative Restarted!"),
        ActionParam("Cancel", {
            DisplayViewMenuHandler.currentMenuTag = breadcrumbEntries.keys.toList()[0]
            MessageChannelHandler.send(ViewPackage.DisplayViewBridge, DisplayViewMessage(DisplayViewMessage.DisplayViewMessageType.Rebuild) )
        })
    )

    override fun tag() = tag
    override fun label() = label

    const val tag = "restartProgressMenu"
    const val label = "Restart Progress"

}