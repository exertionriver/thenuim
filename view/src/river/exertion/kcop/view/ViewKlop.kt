package river.exertion.kcop.view

import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import river.exertion.kcop.asset.AssetManagerHandler
import river.exertion.kcop.asset.klop.IAssetKlop
import river.exertion.kcop.asset.view.ColorPalette
import river.exertion.kcop.base.Id
import river.exertion.kcop.ecs.EngineHandler
import river.exertion.kcop.ecs.klop.IECSKlop
import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.messaging.klop.IMessagingKlop
import river.exertion.kcop.view.asset.*
import river.exertion.kcop.view.klop.IMenuKlop
import river.exertion.kcop.view.menu.DisplayViewMenuHandler
import river.exertion.kcop.view.menu.MainMenu
import river.exertion.kcop.view.messaging.KcopSimulationMessage
import river.exertion.kcop.view.system.TimeLogSystem

object ViewKlop : IMessagingKlop, IAssetKlop, IECSKlop, IMenuKlop {

    override var id = Id.randomId()

    override var name = this::class.simpleName.toString()

    override fun load() {
        loadChannels()
        loadAssets()
        loadSystems()
        loadMenus()
    }

    override fun unload() { }

    override fun loadChannels() {
        MessageChannelHandler.addChannel(MessageChannel(KcopBridge, KcopSimulationMessage::class))
    }

    override fun loadAssets() {
        AssetManagerHandler.assets.setLoader(FreeTypeFontGenerator::class.java, FreeTypeFontGeneratorLoader(AssetManagerHandler.ifhr))
        AssetManagerHandler.assets.setLoader(BitmapFont::class.java, ".ttf", FreetypeFontLoader(AssetManagerHandler.ifhr))

        AssetManagerHandler.loadAssetsByPath<BitmapFont>(FreeTypeFontAssetStore.values().map { it.path }, FreeTypeFontAssetStore.values().map { it.ftflp() } )
        AssetManagerHandler.loadAssetsByPath<Texture>(TextureAssetStore.values().map { it.path } )
        AssetManagerHandler.loadAssetsByPath<Skin>(SkinAssetStore.values().map { it.path } )
        AssetManagerHandler.loadAssetsByPath<Music>(SoundAssetStore.values().map { it.path } )
        AssetManagerHandler.loadAssetsByPath<Music>(MusicAssetStore.values().map { it.path } )

        KcopSkin.skin = AssetManagerHandler.getAsset<Skin>(SkinAssetStore.KcopUi.path)
        KcopSkin.uiSounds[KcopSkin.UiSounds.Click] = AssetManagerHandler.getAsset<Music>(SoundAssetStore.Click.path)
        KcopSkin.uiSounds[KcopSkin.UiSounds.Enter] = AssetManagerHandler.getAsset<Music>(SoundAssetStore.Enter.path)
        KcopSkin.uiSounds[KcopSkin.UiSounds.Swoosh] = AssetManagerHandler.getAsset<Music>(SoundAssetStore.Swoosh.path)
    }

    override fun loadSystems() {
        EngineHandler.addSystem(TimeLogSystem())
    }

    override fun loadMenus() {
        DisplayViewMenuHandler.addMenu(MainMenu)
        DisplayViewMenuHandler.currentMenuTag = MainMenu.tag
    }

    override fun dispose() {
        SdcHandler.dispose()
        KcopSkin.dispose()
    }

    const val KcopBridge = "KcopBridge"
    val MainMenuBackgroundColor = ColorPalette.of("Color101")
    val MainMenuText = ColorPalette.of("Color443")
}