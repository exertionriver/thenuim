package river.exertion.kcop.profile

import com.badlogic.gdx.assets.AssetManager
import river.exertion.kcop.asset.AssetManagerHandler.lfhr
import river.exertion.kcop.messaging.Id
import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.plugin.IPackage
import river.exertion.kcop.plugin.IPlugin
import river.exertion.kcop.profile.asset.ProfileAsset
import river.exertion.kcop.profile.asset.ProfileAssetLoader
import river.exertion.kcop.profile.asset.ProfileAssets
import river.exertion.kcop.profile.menu.*
import river.exertion.kcop.profile.messaging.ProfileMenuDataMessage
import river.exertion.kcop.profile.messaging.ProfileMessage
import river.exertion.kcop.view.ViewPackage.MenuViewBridge
import river.exertion.kcop.view.menu.DisplayViewMenuHandler
import river.exertion.kcop.view.menu.MainMenu
import river.exertion.kcop.view.messaging.MenuViewMessage
import river.exertion.kcop.view.messaging.menuParams.ActionParam

class ProfilePackage : IPackage {
    override var id = Id.randomId()
    override var name = this::class.simpleName.toString()

    var profileAssets = ProfileAssets()

    override fun loadAssets(assetManager: AssetManager) {
        assetManager.setLoader(ProfileAsset::class.java, ProfileAssetLoader(lfhr))
        profileAssets.reload()
    }

    override fun loadChannels() {
        MessageChannelHandler.addChannel(MessageChannel(ProfileBridge, ProfileMessage::class))
        MessageChannelHandler.addChannel(MessageChannel(ProfileMenuDataBridge, ProfileMenuDataMessage::class))
    }

    override fun loadMenus() {
        DisplayViewMenuHandler.addMenu(LoadProfileMenu())
        DisplayViewMenuHandler.addMenu(NewProfileMenu())
        DisplayViewMenuHandler.addMenu(ProfileMenu())
        DisplayViewMenuHandler.addMenu(ProfileSettingsMenu())
        DisplayViewMenuHandler.addMenu(RestartProgressMenu())
        DisplayViewMenuHandler.addMenu(SaveProgressMenu())

        MainMenu.assignableNavs.add(
            ActionParam("Profile >", {
    //            MessageChannelEnum.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.ReloadMenuProfiles))
                MessageChannelHandler.send(MenuViewBridge, MenuViewMessage(ProfileMenu.tag) )
        }))
        MainMenu.assignableNavs.add(
            ActionParam("Settings >", {
    //            MessageChannelEnum.AMH_LOAD_BRIDGE.send(null, AMHLoadMessage(AMHLoadMessage.AMHLoadMessageType.UpdateSelectedProfileFromComponents))
                MessageChannelHandler.send(MenuViewBridge, MenuViewMessage(ProfileSettingsMenu.tag) )
        }))
        MainMenu.assignableNavs.add(
            ActionParam("Save Progress >", {
    //            MessageChannelEnum.AMH_SAVE_BRIDGE.send(null, AMHSaveMessage(AMHSaveMessage.AMHSaveMessageType.PrepSaveProgress))
                MessageChannelHandler.send(MenuViewBridge, MenuViewMessage(SaveProgressMenu.tag) )
        }))
    }

    override fun loadSystems() {}

    override fun dispose() {}

    companion object {
        const val ProfileBridge = "ProfileBridge"
        const val ProfileMenuDataBridge = "ProfileMenuDataBridge"
    }
}