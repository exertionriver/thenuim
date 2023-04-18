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
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import kotlinx.serialization.json.Json
import ktx.assets.load
import ktx.assets.unloadSafely
import ktx.collections.gdxArrayOf
import river.exertion.kcop.assets.AssetManagerMessageHandler.messageHandler
import river.exertion.kcop.simulation.view.FontPackage
import river.exertion.kcop.system.ecs.SystemManager.logDebug
import river.exertion.kcop.system.ecs.component.NarrativeComponent
import river.exertion.kcop.system.ecs.component.ProfileComponent
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.Switchboard
import kotlin.io.path.Path
import kotlin.io.path.listDirectoryEntries

class AssetManagerHandler : Telegraph {

    val assets = AssetManager()
    val lfhr = LocalFileHandleResolver()
    val ifhr = InternalFileHandleResolver()

    var profileAssets = ProfileAssets()
    var narrativeAssets = NarrativeAssets()
    var narrativeImmersionAssets = NarrativeImmersionAssets()

    var selectedProfileAsset : ProfileAsset? = null
    var selectedNarrativeAsset : NarrativeAsset? = null
    var selectedImmersionAsset : NarrativeImmersionAsset? = null

    var currentProfileComponent : ProfileComponent? = null
    var currentImmersionComponent : NarrativeComponent? = null

    init {
        assets.setLoader(FreeTypeFontGenerator::class.java, FreeTypeFontGeneratorLoader(ifhr))
        assets.setLoader(BitmapFont::class.java, ".ttf", FreetypeFontLoader(ifhr))
        FreeTypeFontAssets.values().forEach { assets.load(it) }
        TextureAssets.values().forEach { assets.load(it) }
        SkinAssets.values().forEach { assets.load(it) }
        assets.finishLoading()

        assets.setLoader(ProfileAsset::class.java, ProfileAssetLoader(lfhr))
        assets.setLoader(NarrativeAsset::class.java, NarrativeAssetLoader(lfhr))
        assets.setLoader(NarrativeImmersionAsset::class.java, NarrativeImmersionAssetLoader(lfhr))

        reloadProfileAssets()
        reloadNarrativeAssets()
        reloadNarrativeImmersionAssets()

        MessageChannel.AMH_LOAD_BRIDGE.enableReceive(this)
        MessageChannel.AMH_SAVE_BRIDGE.enableReceive(this)
    }

    fun fontPackage() : FontPackage {
        return FontPackage(
                assets[FreeTypeFontAssets.NotoSansSymbolsSemiBoldText].apply { this.data.setScale(FreeTypeFontAssets.NotoSansSymbolsSemiBoldText.baseFontSize().fontScale())},
                assets[FreeTypeFontAssets.ImmortalSmall].apply { this.data.setScale(FreeTypeFontAssets.ImmortalSmall.baseFontSize().fontScale())},
                assets[FreeTypeFontAssets.ImmortalMedium].apply { this.data.setScale(FreeTypeFontAssets.ImmortalMedium.baseFontSize().fontScale())},
                assets[FreeTypeFontAssets.ImmortalLarge].apply { this.data.setScale(FreeTypeFontAssets.ImmortalLarge.baseFontSize().fontScale())}
        )
    }

    fun skin() : Skin = assets[SkinAssets.CleanCrispyUi]

    inline fun <reified T:IAsset>reloadAssets(assetLoadLocation : String): List<T> {

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

    fun reloadProfileAssets()  {
        profileAssets.values = reloadAssets<ProfileAsset>(ProfileAssets.profileAssetLocation).toMutableList()
    }

    fun reloadNarrativeAssets() {
        narrativeAssets.values = reloadAssets<NarrativeAsset>(NarrativeAssets.narrativeAssetLocation).toMutableList()
    }


    fun reloadNarrativeImmersionAssets() {
        narrativeImmersionAssets.values = reloadAssets<NarrativeImmersionAsset>(NarrativeImmersionAssets.narrativeImmersionAssetLocation).toMutableList()
    }

    fun loadedProfileAssetTitles() : List<String> {
        return if (profileAssets.values.isNotEmpty()) profileAssets.values.map { it.assetTitle() } else listOf("<no profiles found>")
    }

    fun loadedNarrativeAssetTitles() : List<String> {
        return if (narrativeAssets.values.isNotEmpty()) narrativeAssets.values.map { it.assetTitle() } else listOf("<no narratives found>")
    }

    fun initSelectedProfile() {
        if (ProfileAsset.isValid(selectedProfileAsset)) {

            //init selected profile
            ProfileComponent.ecsInit(selectedProfileAsset!!.profile!!)

            //reload in prep to init
            reloadNarrativeAssets()
            selectedNarrativeAsset = narrativeAssets.byId(selectedProfileAsset!!.profile!!.currentImmersionId)

            //init current narrative immersion
            initSelectedNarrative()
        } else {
            Switchboard.noloadProfile()
        }

    }

    fun initSelectedNarrative() {
        if (NarrativeAsset.isValid(selectedNarrativeAsset) ) {

            if (ProfileAsset.isValid(selectedProfileAsset)) {
                //reload in prep to init
                reloadNarrativeImmersionAssets()
                selectedImmersionAsset = narrativeImmersionAssets.byIds(selectedProfileAsset!!.assetId(), selectedNarrativeAsset!!.assetId())
            } else {
                selectedProfileAsset = ProfileAsset.new(currentProfileComponent!!, currentImmersionComponent)
            }

            //init selected narrative
            NarrativeComponent.ecsInit(selectedProfileAsset!!.profile!!, selectedNarrativeAsset!!.narrative!!, selectedImmersionAsset?.narrativeImmersion)
        } else {
            Switchboard.noloadNarrative()
        }
    }

    override fun handleMessage(msg: Telegram?): Boolean = this.messageHandler(msg)

    companion object {
        val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

        const val NoProfileLoaded = "No Profile Loaded"
        const val NoNarrativeLoaded = "No Narrative Loaded"
    }

    fun dispose() {
        assets.dispose()
    }
}