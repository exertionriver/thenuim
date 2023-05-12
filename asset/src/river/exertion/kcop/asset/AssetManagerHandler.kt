package river.exertion.kcop.asset

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import kotlinx.serialization.json.Json
import ktx.assets.load
import ktx.assets.unloadSafely
import ktx.collections.gdxArrayOf
import river.exertion.kcop.asset.view.FontPackage
import river.exertion.kcop.asset.view.KcopSkin
import river.exertion.kcop.messaging.Id.Companion.logDebug
import kotlin.io.path.Path
import kotlin.io.path.listDirectoryEntries

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

        KcopSkin.skin = assets[SkinAssets.KcopUi]
        KcopSkin.fontPackage = fontPackage()
        KcopSkin.uiSounds[KcopSkin.UiSounds.Click] = assets[SoundAssets.Click]
        KcopSkin.uiSounds[KcopSkin.UiSounds.Enter] = assets[SoundAssets.Enter]
        KcopSkin.uiSounds[KcopSkin.UiSounds.Swoosh] = assets[SoundAssets.Swoosh]
    }

    fun fontPackage() : FontPackage {
        return FontPackage(
            assets[FreeTypeFontAssets.NotoSansSymbolsSemiBoldText].apply { this.data.setScale(FreeTypeFontAssets.NotoSansSymbolsSemiBoldText.baseFontSize().fontScale())},
            assets[FreeTypeFontAssets.ImmortalSmall].apply { this.data.setScale(FreeTypeFontAssets.ImmortalSmall.baseFontSize().fontScale())},
            assets[FreeTypeFontAssets.ImmortalMedium].apply { this.data.setScale(FreeTypeFontAssets.ImmortalMedium.baseFontSize().fontScale())},
            assets[FreeTypeFontAssets.ImmortalLarge].apply { this.data.setScale(FreeTypeFontAssets.ImmortalLarge.baseFontSize().fontScale())}
        )
    }

    inline fun <reified T: IAsset>reloadLocalAssets(assetLoadLocation : String): List<T> {

        //remove previous assets of type T
        val previousAssetArray = gdxArrayOf<T>()

        assets.getAll(T::class.java, previousAssetArray)
        previousAssetArray.forEach {
            assets.unloadSafely(it.assetPath)
        }

        //TODO: exception handling for path creation
        //reload assets of type T
        Path(assetLoadLocation).listDirectoryEntries().forEach {
            assets.load<T>(it.toString())
        }
        assets.finishLoading()

        val currentAssetArray = gdxArrayOf<T>()
        assets.getAll(T::class.java, currentAssetArray)

        //log any load errors
        currentAssetArray.filter { it.status != null}.forEach {
            logDebug("${it.status}", "${it.statusDetail}")
        }

        return currentAssetArray.toMutableList()
    }

    const val NoProfileLoaded = "No Profile Loaded"
    const val NoImmersionLoaded = "No Immersion Loaded"
}