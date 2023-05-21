package river.exertion.kcop.profile

import river.exertion.kcop.asset.AssetManagerHandler
import river.exertion.kcop.asset.AssetManagerHandler.lfhr
import river.exertion.kcop.ecs.ECSPackage
import river.exertion.kcop.ecs.component.IRLTimeComponent
import river.exertion.kcop.ecs.entity.SubjectEntity
import river.exertion.kcop.ecs.messaging.EngineComponentMessage
import river.exertion.kcop.messaging.Id
import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.plugin.IKcopPackage
import river.exertion.kcop.profile.asset.ProfileAsset
import river.exertion.kcop.profile.asset.ProfileAsset.Companion.currentProfileAsset
import river.exertion.kcop.profile.asset.ProfileAssetLoader
import river.exertion.kcop.profile.component.ProfileComponent
import river.exertion.kcop.profile.menu.*
import river.exertion.kcop.profile.messaging.ProfileComponentMessage
import river.exertion.kcop.view.menu.DisplayViewMenuHandler
import river.exertion.kcop.view.menu.MainMenu
import river.exertion.kcop.view.menu.MenuActionParam

object ProfilePackage : IKcopPackage {

    override var id = Id.randomId()
    override var name = this::class.simpleName.toString()

    override fun loadAssets() {
        AssetManagerHandler.assets.setLoader(ProfileAsset::class.java, ProfileAssetLoader(lfhr))
    }

    override fun loadChannels() {
        MessageChannelHandler.addChannel(MessageChannel(ProfileBridge, ProfileComponentMessage::class))
    }

    override fun loadMenus() {
        DisplayViewMenuHandler.addMenu(LoadProfileMenu)
        DisplayViewMenuHandler.addMenu(NewProfileMenu)
        DisplayViewMenuHandler.addMenu(ProfileMenu)
        DisplayViewMenuHandler.addMenu(ProfileSettingsMenu)
        DisplayViewMenuHandler.addMenu(RestartProgressMenu)
        DisplayViewMenuHandler.addMenu(SaveProgressMenu)

        MainMenu.assignableNavs.add(
            MenuActionParam("Profile >", {
                DisplayViewMenuHandler.currentMenuTag = ProfileMenu.tag
            }))
        MainMenu.assignableNavs.add(
            MenuActionParam("Settings >", {
                ProfileSettingsMenu.settings = currentProfileAsset.settings
                DisplayViewMenuHandler.currentMenuTag = ProfileSettingsMenu.tag
            }))
        MainMenu.assignableNavs.add(
            MenuActionParam("Save Progress >", {
                DisplayViewMenuHandler.currentMenuTag = SaveProgressMenu.tag
            }))
    }

    override fun loadSystems() {
        MessageChannelHandler.send(
                ECSPackage.EngineComponentBridge, EngineComponentMessage(
                EngineComponentMessage.EngineComponentMessageType.ReplaceComponent,
                SubjectEntity.entityName, IRLTimeComponent::class.java)
        )

        ProfileComponent.ecsInit()
    }

    override fun dispose() {}

    const val ProfileBridge = "ProfileBridge"
}