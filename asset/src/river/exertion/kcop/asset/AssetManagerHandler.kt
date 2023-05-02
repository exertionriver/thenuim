package river.exertion.kcop.asset

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import kotlinx.serialization.json.Json
import river.exertion.kcop.view.FontPackage
import river.exertion.kcop.view.KcopSkin

object AssetManagerHandler {

    val assets = AssetManager()
    val lfhr = LocalFileHandleResolver()
    val ifhr = InternalFileHandleResolver()

    val json = Json { ignoreUnknownKeys = true; encodeDefaults = true; isLenient = true }

    init {
        assets.setLoader(FreeTypeFontGenerator::class.java, FreeTypeFontGeneratorLoader(ifhr))
        assets.setLoader(BitmapFont::class.java, ".ttf", FreetypeFontLoader(ifhr))
        FreeTypeFontAssets.values().forEach { assets.load(it) }
        TextureAssets.values().forEach { assets.load(it) }
        SkinAssets.values().forEach { assets.load(it) }
        SoundAssets.values().forEach { assets.load(it) }

        assets.finishLoading()
    }

    fun fontPackage() : FontPackage {
        return FontPackage(
            assets[FreeTypeFontAssets.NotoSansSymbolsSemiBoldText].apply { this.data.setScale(FreeTypeFontAssets.NotoSansSymbolsSemiBoldText.baseFontSize().fontScale())},
            assets[FreeTypeFontAssets.ImmortalSmall].apply { this.data.setScale(FreeTypeFontAssets.ImmortalSmall.baseFontSize().fontScale())},
            assets[FreeTypeFontAssets.ImmortalMedium].apply { this.data.setScale(FreeTypeFontAssets.ImmortalMedium.baseFontSize().fontScale())},
            assets[FreeTypeFontAssets.ImmortalLarge].apply { this.data.setScale(FreeTypeFontAssets.ImmortalLarge.baseFontSize().fontScale())}
        )
    }

    fun kcopSkin() : KcopSkin = KcopSkin(assets[SkinAssets.KcopUi], fontPackage()).apply {
        this.uiSounds[KcopSkin.UiSounds.Click] = assets[SoundAssets.Click]
        this.uiSounds[KcopSkin.UiSounds.Enter] = assets[SoundAssets.Enter]
        this.uiSounds[KcopSkin.UiSounds.Swoosh] = assets[SoundAssets.Swoosh]
       // DisplayViewLayoutAssets.values().forEach { this.layouts.add(assets[it].DVLayout!!) }
    }
}