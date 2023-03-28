package river.exertion.kcop.assets

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import ktx.assets.load
import ktx.assets.unloadSafely
import ktx.collections.gdxArrayOf
import river.exertion.kcop.simulation.view.FontPackage
import river.exertion.kcop.simulation.view.displayViewMenus.params.ProfileMenuParams
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.AMHMessage
import river.exertion.kcop.system.messaging.messages.MenuMessage
import kotlin.io.path.Path
import kotlin.io.path.listDirectoryEntries

class AssetManagerHandler : Telegraph {

    val assets = AssetManager()
    val lfhr = LocalFileHandleResolver()
    val ifhr = InternalFileHandleResolver()

    init {
        assets.setLoader(FreeTypeFontGenerator::class.java, FreeTypeFontGeneratorLoader(ifhr))
        assets.setLoader(BitmapFont::class.java, ".ttf", FreetypeFontLoader(ifhr))
        FreeTypeFontAssets.values().forEach { assets.load(it) }
        TextureAssets.values().forEach { assets.load(it) }
        assets.finishLoading()

        assets.setLoader(ProfileAsset::class.java, ProfileAssetLoader(lfhr))
        assets.setLoader(NarrativeAsset::class.java, NarrativeAssetLoader(lfhr))
        loadProfileAssets()
        loadNarrativeAssets()

        MessageChannel.AMH_BRIDGE.enableReceive(this)
    }

    inline fun <reified T:LoadableAsset>reloadAssets(assetLoadLocation : String): Map<String, T> {

        val returnAssets = mutableMapOf<String, T>()

        val previousAssetArray = gdxArrayOf<T>()
        assets.getAll(T::class.java, previousAssetArray)

        previousAssetArray.forEach {
            assets.unloadSafely(it.assetPath)
        }

        Path(assetLoadLocation).listDirectoryEntries().forEach {
            assets.load<T>(it.toString())
        }
        assets.finishLoading()

        val currentAssetArray = gdxArrayOf<T>()
        assets.getAll(T::class.java, currentAssetArray)

        currentAssetArray.forEach {
            if (it.status == null) returnAssets[it.assetPath] = it
        }

        return returnAssets
    }

    fun loadProfileAssets() = reloadAssets<ProfileAsset>(ProfileAssets.profileAssetLocation)
    fun loadNarrativeAssets() = reloadAssets<NarrativeAsset>(NarrativeAssets.narrativeAssetLocation)

    fun fontPackage() : FontPackage {
        return FontPackage(
            assets[FreeTypeFontAssets.NotoSansSymbolsSemiBoldText].apply { this.data.setScale(FreeTypeFontAssets.NotoSansSymbolsSemiBoldText.baseFontSize().fontScale())},
            assets[FreeTypeFontAssets.ImmortalSmall].apply { this.data.setScale(FreeTypeFontAssets.ImmortalSmall.baseFontSize().fontScale())},
            assets[FreeTypeFontAssets.ImmortalMedium].apply { this.data.setScale(FreeTypeFontAssets.ImmortalMedium.baseFontSize().fontScale())},
            assets[FreeTypeFontAssets.ImmortalLarge].apply { this.data.setScale(FreeTypeFontAssets.ImmortalLarge.baseFontSize().fontScale())}
        )
    }

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                (MessageChannel.AMH_BRIDGE.isType(msg.message) ) -> {
                    val amhMessage: AMHMessage = MessageChannel.AMH_BRIDGE.receiveMessage(msg.extraInfo)

                    when (amhMessage.messageType) {
                        AMHMessage.AMHMessageType.ReloadMenuProfiles -> {
                            val loadedProfileAssets = loadProfileAssets().values.toList()
                            val loadedNarrativeAssets = loadNarrativeAssets().values.toList()
                            MessageChannel.MENU_BRIDGE.send(null, MenuMessage(null, ProfileMenuParams(loadedProfileAssets, loadedNarrativeAssets)))
                        }
                        AMHMessage.AMHMessageType.SaveProfile -> {
                            if (amhMessage.saveProfileAsset != null) {
                                amhMessage.saveProfileAsset.save()
                            }
                        }
                    }

                    return true
                }
            }
        }
        return false
    }

    enum class SaveType() {
        Merge, Overwrite
    }

    fun dispose() {
        assets.dispose()
    }
}