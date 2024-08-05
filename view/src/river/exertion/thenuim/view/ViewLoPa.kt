package river.exertion.thenuim.view

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import river.exertion.thenuim.asset.AssetManagerHandler
import river.exertion.thenuim.asset.IAssetLoPa
import river.exertion.thenuim.asset.view.ColorPalette
import river.exertion.thenuim.base.Id
import river.exertion.thenuim.base.TnmBase
import river.exertion.thenuim.ecs.EngineHandler
import river.exertion.thenuim.ecs.IECSLoPa
import river.exertion.thenuim.messaging.MessageChannel
import river.exertion.thenuim.messaging.MessageChannelHandler
import river.exertion.thenuim.messaging.IMessagingLoPa
import river.exertion.thenuim.view.asset.*
import river.exertion.thenuim.view.menu.DisplayViewMenuHandler
import river.exertion.thenuim.view.menu.MainMenu
import river.exertion.thenuim.view.messaging.TnmSimMessage
import river.exertion.thenuim.view.plugin.DisplayViewPluginAsset
import river.exertion.thenuim.view.plugin.DisplayViewPluginAssetLoader
import river.exertion.thenuim.view.system.TimeLogSystem

object ViewLoPa : IMessagingLoPa, IAssetLoPa, IECSLoPa, IMenuLoPa {

    override val id = Id.randomId()
    override val tag = this::class.simpleName.toString()
    override val name = TnmBase.appName
    override val version = TnmBase.appVersion

    override fun load() {
        loadChannels()
        loadAssets()
        loadSystems()
        loadMenus()

        TnmBase.inputMultiplexer.addProcessor(TnmBase.stage)
        TnmBase.inputMultiplexer.addProcessor(TnmInputProcessor)
    }

    override fun unload() {}

    override fun loadChannels() {
        MessageChannelHandler.addChannel(MessageChannel(TnmBridge, TnmSimMessage::class))
    }

    override fun loadAssets() {
        //internal fonts
        AssetManagerHandler.assets.setLoader(FreeTypeFontGenerator::class.java, FreeTypeFontGeneratorLoader(
            AssetManagerHandler.ifhr))
        AssetManagerHandler.assets.setLoader(BitmapFont::class.java, ".ttf", FreetypeFontLoader(AssetManagerHandler.ifhr))
        FreeTypeFontAssetStore.loadAll() //for internal assets
        FreeTypeFontAssets.reload() // for external assets

        TnmFont.TEXT.font = FreeTypeFontAssetStore.NotoSansSymbolsSemiBoldText.get()
        TnmFont.SMALL.font = FreeTypeFontAssetStore.ImmortalSmall.get()
        TnmFont.MEDIUM.font = FreeTypeFontAssetStore.ImmortalMedium.get()
        TnmFont.LARGE.font = FreeTypeFontAssetStore.ImmortalLarge.get()

        TextureAssetStore.loadAll()
        SkinAssetStore.loadAll()
        SoundAssetStore.loadAll()
        MusicAssetStore.loadAll()

        TnmSkin.skin = SkinAssetStore.TnmUi.get()
        TnmSkin.uiSounds[TnmSkin.UiSounds.Click] = SoundAssetStore.Click.get()
        TnmSkin.uiSounds[TnmSkin.UiSounds.Enter] = SoundAssetStore.Enter.get()
        TnmSkin.uiSounds[TnmSkin.UiSounds.Swoosh] = SoundAssetStore.Swoosh.get()

        //display view plugins
        AssetManagerHandler.assets.setLoader(DisplayViewPluginAsset::class.java, DisplayViewPluginAssetLoader(
            AssetManagerHandler.lfhr) )
    }

    override fun loadSystems() {
        EngineHandler.addSystem(TimeLogSystem())
    }

    override fun unloadSystems() { }

    override fun loadMenus() {
        DisplayViewMenuHandler.addMenu(MainMenu)
        DisplayViewMenuHandler.currentMenuTag = MainMenu.tag
    }

    override fun dispose() {
        SdcHandler.dispose()
        TnmSkin.dispose()

        super<IMessagingLoPa>.dispose()
        super<IAssetLoPa>.dispose()
        super<IECSLoPa>.dispose()
    }

    const val TnmBridge = "TnmBridge"
    val MainMenuBackgroundColor = ColorPalette.of("Color101")
    val MainMenuText = ColorPalette.of("Color443")
}