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
import river.exertion.kcop.simulation.view.ctrl.LogViewCtrl
import river.exertion.kcop.simulation.view.ctrl.TextViewCtrl
import river.exertion.kcop.simulation.view.displayViewMenus.params.NarrativeMenuDataParams
import river.exertion.kcop.simulation.view.displayViewMenus.params.ProfileMenuDataParams
import river.exertion.kcop.system.ecs.component.ImmersionTimerComponent
import river.exertion.kcop.system.ecs.component.NarrativeComponent
import river.exertion.kcop.system.ecs.component.ProfileComponent
import river.exertion.kcop.system.ecs.entity.ProfileEntity
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.*
import river.exertion.kcop.system.profile.Profile
import kotlin.io.path.Path
import kotlin.io.path.listDirectoryEntries

class AssetManagerHandler : Telegraph {

    val assets = AssetManager()
    val lfhr = LocalFileHandleResolver()
    val ifhr = InternalFileHandleResolver()

    var narrativeAssets : Map<String, NarrativeAsset>
    var profileAssets : Map<String, ProfileAsset>

    var selectedProfileAsset : ProfileAsset? = null
    var selectedNarrativeAsset : NarrativeAsset? = null

    var currentProfile : Profile? = null

    init {
        assets.setLoader(FreeTypeFontGenerator::class.java, FreeTypeFontGeneratorLoader(ifhr))
        assets.setLoader(BitmapFont::class.java, ".ttf", FreetypeFontLoader(ifhr))
        FreeTypeFontAssets.values().forEach { assets.load(it) }
        TextureAssets.values().forEach { assets.load(it) }
        assets.finishLoading()

        assets.setLoader(ProfileAsset::class.java, ProfileAssetLoader(lfhr))
        assets.setLoader(NarrativeAsset::class.java, NarrativeAssetLoader(lfhr))
        profileAssets = loadProfileAssets()
        narrativeAssets = loadNarrativeAssets()

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

    fun loadProfileAssets(reloadProfileName : String? = null) : Map<String, ProfileAsset> {
        profileAssets = reloadAssets(ProfileAssets.profileAssetLocation)
        selectedProfileAsset = profileAssets.entries.firstOrNull { it.value.profileAssetName() == reloadProfileName }?.value
        currentProfile = selectedProfileAsset?.profile
        return profileAssets
    }
    fun loadNarrativeAssets(reloadNarrativeName : String? = null) : Map<String, NarrativeAsset> {
        narrativeAssets = reloadAssets(NarrativeAssets.narrativeAssetLocation)
        selectedNarrativeAsset = narrativeAssets.entries.firstOrNull { it.value.narrativeAssetName() == reloadNarrativeName }?.value
        return narrativeAssets
    }

    fun fontPackage() : FontPackage {
        return FontPackage(
            assets[FreeTypeFontAssets.NotoSansSymbolsSemiBoldText].apply { this.data.setScale(FreeTypeFontAssets.NotoSansSymbolsSemiBoldText.baseFontSize().fontScale())},
            assets[FreeTypeFontAssets.ImmortalSmall].apply { this.data.setScale(FreeTypeFontAssets.ImmortalSmall.baseFontSize().fontScale())},
            assets[FreeTypeFontAssets.ImmortalMedium].apply { this.data.setScale(FreeTypeFontAssets.ImmortalMedium.baseFontSize().fontScale())},
            assets[FreeTypeFontAssets.ImmortalLarge].apply { this.data.setScale(FreeTypeFontAssets.ImmortalLarge.baseFontSize().fontScale())}
        )
    }

    fun loadSelectedProfile() {
        if (selectedProfileAsset != null) {

            //inactivate current narrative
            MessageChannel.NARRATIVE_BRIDGE.send(null, NarrativeMessage(NarrativeMessage.NarrativeMessageType.INACTIVATE))

            //add selected profile
            MessageChannel.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(
                    EngineComponentMessageType.REPLACE_COMPONENT,
                    ProfileEntity.entityName, ProfileComponent::class.java, null,

                    ProfileComponent.ProfileComponentInit(selectedProfileAsset!!)
            ) )

            MessageChannel.PROFILE_BRIDGE.send(null, ProfileMessage(ProfileMessage.ProfileMessageType.LOAD_AMH_WITH_CURRENT))

            loadNarrativeAssets(selectedProfileAsset!!.profile!!.currentImmersionName)

            loadSelectedNarrative()
        } else {
            MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.LogEntry, LogViewCtrl.NoProfileLoaded))
        }

    }

    fun loadSelectedNarrative() {
        if (selectedNarrativeAsset != null && currentProfile != null) {

            //inactivate current narrative
            MessageChannel.NARRATIVE_BRIDGE.send(null, NarrativeMessage(NarrativeMessage.NarrativeMessageType.INACTIVATE))

            //add current narrative
            MessageChannel.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(
                EngineComponentMessageType.REPLACE_COMPONENT,
                ProfileEntity.entityName, NarrativeComponent::class.java, null,

                NarrativeComponent.NarrativeComponentInit(selectedNarrativeAsset!!,
                    currentProfile!!.immersionBlockId(selectedNarrativeAsset!!.narrative!!.name),
                    currentProfile!!.immersionCumlTime(selectedNarrativeAsset!!.narrative!!.name)
                )
            ) )

        } else {
            MessageChannel.TEXT_VIEW_BRIDGE.send(null, TextViewMessage(TextViewCtrl.NoNarrativeLoaded))
            MessageChannel.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(
                EngineComponentMessageType.REMOVE_COMPONENT,
                ProfileEntity.entityName, ImmersionTimerComponent::class.java))
            MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.ResetTime))
        }
    }

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                (MessageChannel.AMH_BRIDGE.isType(msg.message) ) -> {
                    val amhMessage: AMHMessage = MessageChannel.AMH_BRIDGE.receiveMessage(msg.extraInfo)

                    when (amhMessage.messageType) {
                        AMHMessage.AMHMessageType.ReloadMenuProfiles -> {
                            val loadedProfileAssets = loadProfileAssets().values.toList()
                            MessageChannel.INTER_MENU_BRIDGE.send(null, MenuDataMessage(ProfileMenuDataParams(loadedProfileAssets.map { it.profileAssetTitle() }, loadedProfileAssets[0].profileAssetTitle())))
                        }
                        AMHMessage.AMHMessageType.ReloadMenuNarratives -> {
                            val loadedNarrativeAssets = loadNarrativeAssets().values.toList()
                            MessageChannel.INTER_MENU_BRIDGE.send(null, MenuDataMessage(null, NarrativeMenuDataParams(loadedNarrativeAssets.map { it.narrativeAssetTitle() }, loadedNarrativeAssets[0].narrativeAssetTitle())))
                        }
                        AMHMessage.AMHMessageType.SetSelectedProfileAsset -> {
                            if (amhMessage.selectedTitle != null) {
                                selectedProfileAsset = profileAssets.values.firstOrNull { it.profileAssetTitle() == amhMessage.selectedTitle }

                                if (selectedProfileAsset != null) {
                                    if (amhMessage.saveName == null) {
                                        MessageChannel.INTER_MENU_BRIDGE.send(null, MenuDataMessage(ProfileMenuDataParams(null, null, selectedProfileAsset!!.profileAssetInfo(), selectedProfileAsset!!.profileAssetName())))
                                    }
                                    else {
                                        MessageChannel.INTER_MENU_BRIDGE.send(null, MenuDataMessage(ProfileMenuDataParams(null, null, selectedProfileAsset!!.profileAssetInfo(currentProfile!!), selectedProfileAsset!!.profileAssetName())))
                                    }
                                }
                            } else {
                                MessageChannel.INTER_MENU_BRIDGE.send(null, MenuDataMessage(ProfileMenuDataParams(null, null, selectedProfileAsset!!.profileAssetInfo(), selectedProfileAsset!!.profileAssetName())))
                            }
                        }
                        AMHMessage.AMHMessageType.SetSelectedNarrativeAsset -> {
                            if (amhMessage.selectedTitle != null) {
                                selectedNarrativeAsset = narrativeAssets.values.firstOrNull { it.narrativeAssetTitle() == amhMessage.selectedTitle }

                                if (selectedNarrativeAsset != null) {
                                    MessageChannel.INTER_MENU_BRIDGE.send(null, MenuDataMessage(null, NarrativeMenuDataParams(null, null, selectedNarrativeAsset!!.narrativeAssetInfo(), selectedNarrativeAsset!!.narrativeAssetName())))
                                }
                            }
                        }
                        AMHMessage.AMHMessageType.LoadSelectedProfile -> {
                            loadSelectedProfile()
                        }
                        AMHMessage.AMHMessageType.LoadSelectedNarrative -> {
                            loadSelectedNarrative()
                        }
                        AMHMessage.AMHMessageType.ReloadCurrentProfile -> {
                            if (amhMessage.currentProfile != null) {
                                currentProfile = amhMessage.currentProfile
                            }
                        }
                        AMHMessage.AMHMessageType.SaveOverwriteProfile -> {
                            if ((currentProfile != null) && (selectedProfileAsset != null) && (amhMessage.saveName != null)) {
                                selectedProfileAsset!!.profile = currentProfile
                                selectedProfileAsset!!.profile!!.name = amhMessage.saveName
                                selectedProfileAsset!!.save()
                            }
                        }
                        AMHMessage.AMHMessageType.SaveMergeProfile -> {}
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