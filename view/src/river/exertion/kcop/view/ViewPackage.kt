package river.exertion.kcop.view

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.BitmapFontLoader
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import ktx.assets.getAsset
import ktx.assets.load
import river.exertion.kcop.asset.AssetManagerHandler
import river.exertion.kcop.asset.view.ColorPalette
import river.exertion.kcop.ecs.system.SystemHandler
import river.exertion.kcop.messaging.Id
import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.plugin.IKcopPackage
import river.exertion.kcop.view.asset.*
import river.exertion.kcop.view.menu.DisplayViewMenuHandler
import river.exertion.kcop.view.menu.MainMenu
import river.exertion.kcop.view.messaging.*
import river.exertion.kcop.view.system.TimeLogSystem

object ViewPackage : IKcopPackage {

    override var id = Id.randomId()

    override var name = this::class.simpleName.toString()

    override fun loadChannels() {
        MessageChannelHandler.addChannel(MessageChannel(KcopBridge, KcopSimulationMessage::class))
    }

    override fun loadAssets() {
        AssetManagerHandler.assets.setLoader(FreeTypeFontGenerator::class.java, FreeTypeFontGeneratorLoader(AssetManagerHandler.ifhr))
        AssetManagerHandler.assets.setLoader(BitmapFont::class.java, ".ttf", FreetypeFontLoader(AssetManagerHandler.ifhr))
        FreeTypeFontAssetStore.values().forEach { AssetManagerHandler.assets.load(it) }
        TextureAssetStore.values().forEach { AssetManagerHandler.assets.load(it) }
        SkinAssetStore.values().forEach { AssetManagerHandler.assets.load(it) }
        SoundAssetStore.values().forEach { AssetManagerHandler.assets.load(it) }
        MusicAssetStore.values().forEach { AssetManagerHandler.assets.load(it) }

        AssetManagerHandler.assets.finishLoading()

        KcopSkin.skin = AssetManagerHandler.assets[SkinAssetStore.KcopUi]
        KcopSkin.uiSounds[KcopSkin.UiSounds.Click] = AssetManagerHandler.assets[SoundAssetStore.Click]
        KcopSkin.uiSounds[KcopSkin.UiSounds.Enter] = AssetManagerHandler.assets[SoundAssetStore.Enter]
        KcopSkin.uiSounds[KcopSkin.UiSounds.Swoosh] = AssetManagerHandler.assets[SoundAssetStore.Swoosh]
    }

    override fun loadSystems() {
        SystemHandler.pooledEngine.addSystem(TimeLogSystem())
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

//asset support

val bfp = BitmapFontLoader.BitmapFontParameter().apply {
    this.genMipMaps = true
    this.minFilter = Texture.TextureFilter.Linear
    this.magFilter = Texture.TextureFilter.Linear
}

fun AssetManager.load(asset: BitmapFontAssetStore) = load<BitmapFont>(asset.path, bfp)
operator fun AssetManager.get(asset: BitmapFontAssetStore) = getAsset<BitmapFont>(asset.path)

fun AssetManager.load(asset: FreeTypeFontAssetStore) = load(asset.path, BitmapFont::class.java,
        FreetypeFontLoader.FreeTypeFontLoaderParameter().apply {
            this.fontParameters.genMipMaps = true
            this.fontParameters.minFilter = Texture.TextureFilter.MipMapLinearLinear
            this.fontParameters.magFilter = Texture.TextureFilter.Linear
            this.fontParameters.characters = this.fontParameters.characters + "↑" + "↓"
            this.fontParameters.size = FontSize.baseFontSize
            this.fontFileName = asset.path
        })

operator fun AssetManager.get(asset: FreeTypeFontAssetStore) = getAsset<BitmapFont>(asset.path).apply { this.data.setScale(
        FontSize.TEXT.fontScale())
}

fun AssetManager.load(asset: MusicAssetStore) = load<Music>(asset.path)
operator fun AssetManager.get(asset: MusicAssetStore) = getAsset<Music>(asset.path)

fun AssetManager.load(asset: SkinAssetStore) = load<Skin>(asset.path)
operator fun AssetManager.get(asset: SkinAssetStore) = getAsset<Skin>(asset.path)

fun AssetManager.load(asset: SoundAssetStore) = load<Music>(asset.path)
operator fun AssetManager.get(asset: SoundAssetStore) = getAsset<Music>(asset.path)

fun AssetManager.load(asset: TextureAssetStore) = load<Texture>(asset.path)
operator fun AssetManager.get(asset: TextureAssetStore) = getAsset<Texture>(asset.path)