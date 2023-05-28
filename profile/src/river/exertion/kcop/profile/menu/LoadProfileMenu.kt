package river.exertion.kcop.profile.menu

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.profile.ProfilePackage
import river.exertion.kcop.profile.ProfilePackage.NoProfileInfoFound
import river.exertion.kcop.profile.ProfilePackage.ProfileMenuBackgroundColor
import river.exertion.kcop.profile.ProfilePackage.ProfileMenuText
import river.exertion.kcop.profile.asset.ProfileAsset
import river.exertion.kcop.profile.component.ProfileComponent
import river.exertion.kcop.profile.messaging.ProfileComponentMessage
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.asset.FontSize
import river.exertion.kcop.view.layout.MenuView
import river.exertion.kcop.view.menu.DisplayViewMenu
import river.exertion.kcop.view.menu.DisplayViewMenuHandler
import river.exertion.kcop.view.menu.MainMenu
import river.exertion.kcop.view.menu.MenuActionParam

object LoadProfileMenu : DisplayViewMenu {

    override val tag = "loadProfileMenu"
    override val label = "Load"

    override val backgroundColor = ProfileMenuBackgroundColor

    override var menuPane = {
        Table().apply {

            if (ProfileAsset.selectedProfileAsset.assetInfo().isNotEmpty()) {
                ProfileAsset.selectedProfileAsset.assetInfo().forEach { profileEntry ->
                    this.add(Label(profileEntry, KcopSkin.labelStyle(FontSize.SMALL, ProfileMenuText))
                        .apply {
                            this.wrap = true
                        }).growX().left()
                    this.row()
                }
//        this.debug()
                this@LoadProfileMenu.actions.firstOrNull { it.label == "Yes" }
                    ?.apply { this.log = "Profile Loaded : ${ProfileAsset.selectedProfileAsset.assetName()}" }
            } else {
                this.add(
                    Label(NoProfileInfoFound, KcopSkin.labelStyle(FontSize.SMALL, ProfileMenuText))
                ).growX().left()
                this@LoadProfileMenu.actions.firstOrNull { it.label == "Yes" }
                    ?.apply { this.label = "Error"; this.action = {} }
            }
            this.top()
        }
    }
    override val breadcrumbEntries = mapOf(
        ProfileMenu.tag to ProfileMenu.label,
        MainMenu.tag to MainMenu.label
    )

    override val assignableNavs = mutableListOf<MenuActionParam>()

    override val actions = mutableListOf(
        MenuActionParam("Yes", {
            MenuView.closeMenu()
            MessageChannelHandler.send(ProfilePackage.ProfileBridge, ProfileComponentMessage(ProfileComponentMessage.ProfileMessageType.Inactivate))
            ProfileAsset.currentProfileAsset = ProfileAsset.selectedProfileAsset
            ProfileComponent.ecsInit()
        }, "Profile Loaded!"),
        //go back a menu
        MenuActionParam("No", {
            DisplayViewMenuHandler.currentMenuTag = breadcrumbEntries.keys.toList()[0]
        })
    )
}