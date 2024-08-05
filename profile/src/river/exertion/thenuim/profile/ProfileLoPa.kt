package river.exertion.thenuim.profile

import com.badlogic.gdx.scenes.scene2d.ui.Label
import river.exertion.thenuim.asset.AssetManagerHandler
import river.exertion.thenuim.asset.AssetManagerHandler.lfhr
import river.exertion.thenuim.base.Id
import river.exertion.thenuim.asset.IAssetLoPa
import river.exertion.thenuim.asset.view.ColorPalette
import river.exertion.thenuim.base.TnmBase
import river.exertion.thenuim.ecs.EngineHandler
import river.exertion.thenuim.ecs.component.IComponent
import river.exertion.thenuim.ecs.component.IrlTimeComponent
import river.exertion.thenuim.ecs.entity.SubjectEntity
import river.exertion.thenuim.ecs.IECSLoPa
import river.exertion.thenuim.messaging.MessageChannel
import river.exertion.thenuim.messaging.MessageChannelHandler
import river.exertion.thenuim.messaging.IMessagingLoPa
import river.exertion.thenuim.profile.asset.ProfileAsset
import river.exertion.thenuim.profile.asset.ProfileAsset.Companion.currentProfileAsset
import river.exertion.thenuim.profile.asset.ProfileAssetLoader
import river.exertion.thenuim.profile.component.ProfileComponent
import river.exertion.thenuim.profile.menu.*
import river.exertion.thenuim.profile.messaging.ProfileComponentMessage
import river.exertion.thenuim.view.TnmSkin
import river.exertion.thenuim.view.TnmFont
import river.exertion.thenuim.view.ViewLoPa
import river.exertion.thenuim.view.IMenuLoPa
import river.exertion.thenuim.view.menu.DisplayViewMenuHandler
import river.exertion.thenuim.view.menu.MainMenu
import river.exertion.thenuim.view.menu.MenuActionParam

object ProfileLoPa : IMessagingLoPa, IAssetLoPa, IECSLoPa, IMenuLoPa {

    override val id = Id.randomId()
    override val tag = this::class.simpleName.toString()
    override val name = TnmBase.appName
    override val version = TnmBase.appVersion


    override fun load() {
        loadChannels()
        loadAssets()
        loadSystems()
        loadMenus()
    }

    override fun unload() { }

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
                this.add(Label("Profile:", TnmSkin.labelStyle(TnmFont.MEDIUM, ProfileMainMenuText))).left()
                this.row()

                if (currentProfileAsset.persisted && currentProfileAsset.assetInfo().isNotEmpty()) {
                    currentProfileAsset.assetInfo().forEach { profileEntry ->
                        this.add(
                            Label(profileEntry, TnmSkin.labelStyle(TnmFont.SMALL, ProfileMainMenuText))
                                .apply {
                                    this.wrap = true
                                }).growX().left()
                        this.row()
                    }
                } else {
                    this.add(
                        Label(NoProfileLoaded, TnmSkin.labelStyle(TnmFont.SMALL, ProfileMainMenuText))
                    ).growX().left()
                }
                this.top()
            }
        }
    }

    override fun loadSystems() {
        EngineHandler.instantiateEntity<SubjectEntity>()

        IComponent.ecsInit<IrlTimeComponent>(SubjectEntity.entityName)
        IComponent.ecsInit<ProfileComponent>(SubjectEntity.entityName)
    }

    override fun unloadSystems() { }

    override fun dispose() {

        ViewLoPa.dispose()
    }

    const val ProfileBridge = "ProfileBridge"
    const val NoProfileLoaded = "No Profile Loaded"
    const val NoProfileInfoFound = "No Profile Info Found"

    val ProfileMenuBackgroundColor = ColorPalette.of("Color011")
    val ProfileMenuText = ColorPalette.of("Color443")

    val ProfileMainMenuText = ColorPalette.of("Color344")
}