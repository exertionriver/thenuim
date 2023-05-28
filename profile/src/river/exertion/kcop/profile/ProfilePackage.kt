package river.exertion.kcop.profile

import com.badlogic.gdx.scenes.scene2d.ui.Label
import river.exertion.kcop.asset.AssetManagerHandler
import river.exertion.kcop.asset.AssetManagerHandler.lfhr
import river.exertion.kcop.asset.view.ColorPalette
import river.exertion.kcop.ecs.EngineHandler
import river.exertion.kcop.ecs.component.IRLTimeComponent
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
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.asset.FontSize
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
        DisplayViewMenuHandler.addMenu(SaveProgressMenu)

        addProfileNavsToMainMenu()
        addProfileInfoToMainMenu()
    }

    private fun addProfileNavsToMainMenu() {
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

    private fun addProfileInfoToMainMenu() {
        val mainMenuPane = MainMenu.menuPane

        MainMenu.menuPane = {
            mainMenuPane().apply {
                this.add(Label("Profile:", KcopSkin.labelStyle(FontSize.MEDIUM, ProfileMainMenuText))).left()
                this.row()

                if (currentProfileAsset.persisted && currentProfileAsset.assetInfo().isNotEmpty()) {
                    currentProfileAsset.assetInfo().forEach { profileEntry ->
                        this.add(
                            Label(profileEntry, KcopSkin.labelStyle(FontSize.SMALL, ProfileMainMenuText))
                                .apply {
                                    this.wrap = true
                                }).growX().left()
                        this.row()
                    }
                } else {
                    this.add(
                        Label(NoProfileLoaded, KcopSkin.labelStyle(FontSize.SMALL, ProfileMainMenuText))
                    ).growX().left()
                }
                this.top()
            }
        }
    }

    override fun loadSystems() {
        EngineHandler.replaceComponent(componentClass = IRLTimeComponent::class.java)

        ProfileComponent.ecsInit()
    }

    override fun dispose() {}

    const val ProfileBridge = "ProfileBridge"
    const val NoProfileLoaded = "No Profile Loaded"
    const val NoProfileInfoFound = "No Profile Info Found"

    val ProfileMenuBackgroundColor = ColorPalette.of("Color011")
    val ProfileMenuText = ColorPalette.of("Color443")

    val ProfileMainMenuText = ColorPalette.of("Color344")
}