package river.exertion.kcop.profile.menu

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.asset.view.ColorPalette
import river.exertion.kcop.ecs.ECSPackage
import river.exertion.kcop.ecs.entity.SubjectEntity
import river.exertion.kcop.ecs.messaging.EngineComponentMessage
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.profile.ProfilePackage
import river.exertion.kcop.profile.component.ProfileComponent
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.ViewPackage
import river.exertion.kcop.view.layout.MenuView
import river.exertion.kcop.view.menu.DisplayViewMenu
import river.exertion.kcop.view.menu.DisplayViewMenuHandler
import river.exertion.kcop.view.menu.MainMenu
import river.exertion.kcop.view.menu.MenuActionParam

object LoadProfileMenu : DisplayViewMenu {

    override val tag = "loadProfileMenu"
    override val label = "Load"

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
            this@LoadProfileMenu.actions.firstOrNull { it.label == "Yes" }?.apply { this.log = "Narrative Loaded : ${ProfilePackage.selectedProfileAsset.assetName()}" }
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

    override val assignableNavs = mutableListOf<MenuActionParam>()

    override val actions = mutableListOf(
        MenuActionParam("Yes", {
            MenuView.closeMenu()
            ProfilePackage.currentProfileAsset = ProfilePackage.selectedProfileAsset
            MessageChannelHandler.send(ECSPackage.EngineComponentBridge, EngineComponentMessage(
                    EngineComponentMessage.EngineComponentMessageType.ReplaceComponent,
                    SubjectEntity.entityName, ProfileComponent::class.java
            ) )
        }, "Profile Loaded!"),
        //go back a menu
        MenuActionParam("No", {
            DisplayViewMenuHandler.currentMenuTag = breadcrumbEntries.keys.toList()[0]
        })
    )
}