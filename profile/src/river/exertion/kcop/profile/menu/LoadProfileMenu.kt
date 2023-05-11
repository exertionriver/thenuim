package river.exertion.kcop.profile.menu

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.profile.ProfilePackage
import river.exertion.kcop.profile.asset.ProfileAsset
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

object LoadProfileMenu : DisplayViewMenu {

    override val backgroundColor = ColorPalette.of("teal")

    override fun menuPane() = Table().apply {

        if (ProfilePackage.selectedProfileAsset.assetInfo().isNotEmpty()) {
            ProfilePackage.selectedProfileAsset.assetInfo().forEach { profileEntry ->
                this.add(Label(profileEntry, KcopSkin.skin)
                        .apply {
                    this.wrap = true
                }).growX().left()
                this.row()
            }
//        this.debug()
            this@LoadProfileMenu.actions.firstOrNull { it.label == "Yes" }?.apply { this.log = "Profile Loaded : ${ProfilePackage.selectedProfileAsset.assetName()}" }
        } else {
            this.add(Label("no profile info found", KcopSkin.skin)
            ).growX().left()
            this@LoadProfileMenu.actions.firstOrNull { it.label == "Yes" }?.apply { this.label = "Error"; this.action = {} }
        }
        this.top()
    }

    override val breadcrumbEntries = mapOf(
        ProfileMenu.tag to ProfileMenu.label,
        MainMenu.tag to MainMenu.label
    )

    override fun navs() = mutableListOf<ActionParam>()

    override val actions = mutableListOf(
        ActionParam("Yes", {
            ViewSwitchboard.closeMenu()
            ProfilePackage.currentProfileAsset = ProfilePackage.selectedProfileAsset
        }, "Profile Loaded!"),
        //go back a menu
        ActionParam("No", {
            DisplayViewMenuHandler.currentMenuTag = breadcrumbEntries.keys.toList()[0]
            MessageChannelHandler.send(ViewPackage.DisplayViewBridge, DisplayViewMessage(DisplayViewMessage.DisplayViewMessageType.Rebuild) )
        })
    )

    override fun tag() = tag
    override fun label() = label

    const val tag = "loadProfileMenu"
    const val label = "Load"
}