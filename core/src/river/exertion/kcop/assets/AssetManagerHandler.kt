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
import kotlinx.serialization.json.Json
import ktx.assets.load
import ktx.assets.unloadSafely
import ktx.collections.gdxArrayOf
import river.exertion.kcop.Id
import river.exertion.kcop.narrative.character.NameTypes
import river.exertion.kcop.narrative.structure.NarrativeImmersion
import river.exertion.kcop.simulation.view.FontPackage
import river.exertion.kcop.simulation.view.ctrl.TextViewCtrl
import river.exertion.kcop.simulation.view.displayViewMenus.params.NarrativeMenuDataParams
import river.exertion.kcop.simulation.view.displayViewMenus.params.ProfileMenuDataParams
import river.exertion.kcop.system.ecs.SystemManager.logDebug
import river.exertion.kcop.system.ecs.component.IRLTimeComponent
import river.exertion.kcop.system.ecs.component.NarrativeComponent
import river.exertion.kcop.system.ecs.component.ProfileComponent
import river.exertion.kcop.system.ecs.entity.ProfileEntity
import river.exertion.kcop.system.immersionTimer.ImmersionTimer
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.Switchboard
import river.exertion.kcop.system.messaging.messages.*
import river.exertion.kcop.system.profile.Profile
import kotlin.io.path.Path
import kotlin.io.path.listDirectoryEntries

class AssetManagerHandler : Telegraph {

    val assets = AssetManager()
    val lfhr = LocalFileHandleResolver()
    val ifhr = InternalFileHandleResolver()

    var profileAssets : Map<String, ProfileAsset>
    var narrativeAssets : Map<String, NarrativeAsset>
    var narrativeImmersionAssets : Map<String, NarrativeImmersionAsset>

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
        assets.finishLoading()

        assets.setLoader(ProfileAsset::class.java, ProfileAssetLoader(lfhr))
        assets.setLoader(NarrativeAsset::class.java, NarrativeAssetLoader(lfhr))
        assets.setLoader(NarrativeImmersionAsset::class.java, NarrativeImmersionAssetLoader(lfhr))

        profileAssets = reloadProfileAssets()
        narrativeAssets = reloadNarrativeAssets()
        narrativeImmersionAssets = reloadNarrativeImmersionAssets()

        MessageChannel.AMH_LOAD_BRIDGE.enableReceive(this)
        MessageChannel.AMH_SAVE_BRIDGE.enableReceive(this)
    }

    inline fun <reified T:LoadableAsset>reloadAssets(assetLoadLocation : String): Map<String, T> {

        val returnAssets = mutableMapOf<String, T>()

        val previousAssetArray = gdxArrayOf<T>()
        assets.getAll(T::class.java, previousAssetArray)

        previousAssetArray.forEach {
            assets.unloadSafely(it.assetPath)
        }

        //TODO: exception handling for path creation
        Path(assetLoadLocation).listDirectoryEntries().forEach {
            assets.load<T>(it.toString())
        }
        assets.finishLoading()

        val currentAssetArray = gdxArrayOf<T>()
        assets.getAll(T::class.java, currentAssetArray)

        currentAssetArray.forEach {
            if (it.status == null) {
                returnAssets[it.assetPath] = it
            } else {
                logDebug("${it.status}", "${it.statusDetail}")
            }
        }

        return returnAssets
    }

    fun reloadProfileAssets() : Map<String, ProfileAsset> {
        profileAssets = reloadAssets(ProfileAssets.profileAssetLocation)
        return profileAssets
    }

    fun setSelectedProfileAssetById(profileId : String? = null) {
        selectedProfileAsset = profileAssets.entries.firstOrNull { it.value.assetId() == profileId }?.value
    }

    fun setSelectedProfileAssetByTitle(profileTitle : String? = null) {
        selectedProfileAsset = profileAssets.entries.firstOrNull { it.value.assetTitle() == profileTitle }?.value
    }

    fun updateSelectedProfileAssetFromSelectedImmersion() {
        selectedProfileAsset?.profile?.currentImmersionName = selectedNarrativeAsset?.assetName()
        selectedProfileAsset?.profile?.currentImmersionTime = selectedImmersionAsset?.cumlImmersionTime()
    }

    fun updateCurrentProfileComponentFromSelectedImmersion() {
        MessageChannel.PROFILE_BRIDGE.send(null, ProfileMessage(ProfileMessage.ProfileMessageType.UPDATE_IMMERSION_ID, selectedNarrativeAsset!!.assetId()))
        MessageChannel.PROFILE_BRIDGE.send(null, ProfileMessage(ProfileMessage.ProfileMessageType.UPDATE_CUML_TIME, selectedImmersionAsset?.cumlImmersionTime()))
    }

    fun updateSelectedProfileAssetFromCurrentImmersion() {
        selectedProfileAsset?.profile = currentProfileComponent?.profile
        selectedProfileAsset?.profile?.currentImmersionName = currentImmersionComponent?.narrativeName()
        selectedProfileAsset?.profile?.currentImmersionTime = currentImmersionComponent?.cumlImmersionTime()
    }

    fun updateSelectedNarrativeAssetFromCurrentImmersion() {
        selectedNarrativeAsset?.narrative = currentImmersionComponent?.narrative
    }

    fun updateSelectedImmersionAssetFromCurrentImmersion() {
        selectedImmersionAsset?.narrativeImmersion?.location = currentImmersionComponent?.location()
        selectedImmersionAsset?.narrativeImmersion?.flags = currentImmersionComponent?.flags!!
    }

    fun reloadNarrativeAssets() : Map<String, NarrativeAsset> {
        narrativeAssets = reloadAssets(NarrativeAssets.narrativeAssetLocation)
        return narrativeAssets
    }

    fun setSelectedNarrativeAssetById(narrativeId : String? = null) {
        selectedNarrativeAsset = narrativeAssets.entries.firstOrNull { it.value.assetId() == narrativeId }?.value
    }

    fun setSelectedNarrativeAssetByTitle(narrativeTitle : String? = null) {
        selectedNarrativeAsset = narrativeAssets.entries.firstOrNull { it.value.assetTitle() == narrativeTitle }?.value
        selectedImmersionAsset = narrativeImmersionAssets.entries.firstOrNull { it.value.assetId() == NarrativeImmersionAsset.genAssetId(selectedProfileAsset?.assetId(), selectedNarrativeAsset?.assetId()) }?.value
    }

    fun reloadNarrativeImmersionAssets() : Map<String, NarrativeImmersionAsset> {
        narrativeImmersionAssets = reloadAssets(NarrativeImmersionAssets.narrativeImmersionAssetLocation)
        return narrativeImmersionAssets
    }

    fun setSelectedNarrativeImmersionByIds(profileId : String? = null, narrativeId : String? = null) {
        selectedImmersionAsset = narrativeImmersionAssets.entries.firstOrNull { it.value.assetId() == NarrativeImmersionAsset.genAssetId(profileId, narrativeId) }?.value
    }

    fun fontPackage() : FontPackage {
        return FontPackage(
            assets[FreeTypeFontAssets.NotoSansSymbolsSemiBoldText].apply { this.data.setScale(FreeTypeFontAssets.NotoSansSymbolsSemiBoldText.baseFontSize().fontScale())},
            assets[FreeTypeFontAssets.ImmortalSmall].apply { this.data.setScale(FreeTypeFontAssets.ImmortalSmall.baseFontSize().fontScale())},
            assets[FreeTypeFontAssets.ImmortalMedium].apply { this.data.setScale(FreeTypeFontAssets.ImmortalMedium.baseFontSize().fontScale())},
            assets[FreeTypeFontAssets.ImmortalLarge].apply { this.data.setScale(FreeTypeFontAssets.ImmortalLarge.baseFontSize().fontScale())}
        )
    }

    fun initProfile(profileAsset : ProfileAsset) {
        MessageChannel.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(
                EngineComponentMessageType.REPLACE_COMPONENT,
                ProfileEntity.entityName, ProfileComponent::class.java,
                ProfileComponent.ProfileComponentInit(profileAsset)
        ) )
        MessageChannel.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(
                EngineComponentMessageType.REPLACE_COMPONENT,
                ProfileEntity.entityName, IRLTimeComponent::class.java
        ) )
    }

    fun initSelectedProfile() {
        if (selectedProfileAsset != null && selectedProfileAsset!!.profile != null) {

            //inactivate current narrative
            MessageChannel.NARRATIVE_BRIDGE.send(null, NarrativeMessage(NarrativeMessage.NarrativeMessageType.INACTIVATE))
            //inactivate current profile
            MessageChannel.PROFILE_BRIDGE.send(null, ProfileMessage(ProfileMessage.ProfileMessageType.INACTIVATE))

            //init selected profile
            initProfile(selectedProfileAsset!!)

            //reload in prep to init
            reloadNarrativeAssets()
            reloadNarrativeImmersionAssets()

            setSelectedNarrativeAssetById(selectedProfileAsset!!.profile!!.currentImmersionId)
            setSelectedNarrativeImmersionByIds(selectedProfileAsset!!.assetId(), selectedProfileAsset!!.profile!!.currentImmersionId)

            initSelectedNarrative()

        } else {
            Switchboard.noloadProfile()
        }

    }

    fun initNarrative(narrativeAsset: NarrativeAsset, narrativeImmersionAsset: NarrativeImmersionAsset?) {
        MessageChannel.ECS_ENGINE_COMPONENT_BRIDGE.send(null, EngineComponentMessage(
                EngineComponentMessageType.REPLACE_COMPONENT,
                ProfileEntity.entityName, NarrativeComponent::class.java,
                NarrativeComponent.NarrativeComponentInit(narrativeAsset, narrativeImmersionAsset)
        ) )
    }

    fun initSelectedNarrative() {

        if (selectedNarrativeAsset != null) {

            //inactivate current narrative
            MessageChannel.NARRATIVE_BRIDGE.send(null, NarrativeMessage(NarrativeMessage.NarrativeMessageType.INACTIVATE))

            //init selected narrative
            initNarrative(selectedNarrativeAsset!!, selectedImmersionAsset)

            updateCurrentProfileComponentFromSelectedImmersion()

        } else {
            Switchboard.noloadNarrative()
        }
    }

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                (MessageChannel.AMH_LOAD_BRIDGE.isType(msg.message) ) -> {
                    val amhLoadMessage: AMHLoadMessage = MessageChannel.AMH_LOAD_BRIDGE.receiveMessage(msg.extraInfo)

                    when (amhLoadMessage.messageType) {
                        AMHLoadMessage.AMHLoadMessageType.ReloadMenuProfiles -> {
                            val loadedProfileAssets = reloadProfileAssets().values.toList()
                            val loadedProfileAssetTitles = if (loadedProfileAssets.isNotEmpty()) loadedProfileAssets.map { it.assetTitle() } else listOf("<no profiles found>")
                            MessageChannel.INTER_MENU_BRIDGE.send(null, MenuDataMessage(ProfileMenuDataParams(loadedProfileAssetTitles, loadedProfileAssetTitles[0])))
                        }
                        AMHLoadMessage.AMHLoadMessageType.ReloadMenuNarratives -> {
                            val loadedNarrativeAssets = reloadNarrativeAssets().values.toList()
                            val loadedNarrativeAssetTitles = if (loadedNarrativeAssets.isNotEmpty()) loadedNarrativeAssets.map { it.assetTitle() } else listOf("<no narratives found>")
                            MessageChannel.INTER_MENU_BRIDGE.send(null, MenuDataMessage(null, NarrativeMenuDataParams(loadedNarrativeAssetTitles, loadedNarrativeAssetTitles[0])))
                        }
                        AMHLoadMessage.AMHLoadMessageType.SetSelectedProfileAsset -> {
                            if (amhLoadMessage.selectedTitle != null) {
                                setSelectedProfileAssetByTitle(amhLoadMessage.selectedTitle)

                                if (selectedProfileAsset != null && selectedProfileAsset!!.profile != null) {
                                    setSelectedNarrativeAssetById(selectedProfileAsset!!.profile!!.currentImmersionId)
                                    setSelectedNarrativeImmersionByIds(selectedProfileAsset!!.profile!!.id, selectedProfileAsset!!.profile!!.currentImmersionId)

                                    updateSelectedProfileAssetFromSelectedImmersion()

                                    MessageChannel.INTER_MENU_BRIDGE.send(null, MenuDataMessage(ProfileMenuDataParams(null, null, selectedProfileAsset!!.assetInfo(), selectedProfileAsset!!.assetName())))
                                }
                            }
                        }
                        AMHLoadMessage.AMHLoadMessageType.SetSelectedNarrativeAsset -> {
                            if (amhLoadMessage.selectedTitle != null) {
                                setSelectedNarrativeAssetByTitle(amhLoadMessage.selectedTitle)

                                if (selectedNarrativeAsset != null) {
                                    MessageChannel.INTER_MENU_BRIDGE.send(null, MenuDataMessage(null, NarrativeMenuDataParams(null, null, selectedNarrativeAsset!!.assetInfo(), selectedNarrativeAsset!!.assetName())))
                                }
                            }
                        }
                        AMHLoadMessage.AMHLoadMessageType.LoadSelectedProfile -> {
                            initSelectedProfile()
                        }
                        AMHLoadMessage.AMHLoadMessageType.LoadSelectedNarrative -> {
                            initSelectedNarrative()
                        }
                        AMHLoadMessage.AMHLoadMessageType.RestartProgress -> {
                            selectedImmersionAsset?.delete()
                            reloadNarrativeImmersionAssets()
                            initSelectedNarrative()
                        }
                    }

                    return true
                }
                (MessageChannel.AMH_SAVE_BRIDGE.isType(msg.message) ) -> {
                    val amhSaveMessage: AMHSaveMessage = MessageChannel.AMH_SAVE_BRIDGE.receiveMessage(msg.extraInfo)

                    when (amhSaveMessage.messageType) {
                        AMHSaveMessage.AMHSaveMessageType.ReloadCurrentProfile -> {
                            if (amhSaveMessage.currentProfileComponent != null) {
                                currentProfileComponent = amhSaveMessage.currentProfileComponent
                            }
                        }
                        AMHSaveMessage.AMHSaveMessageType.ReloadCurrentImmersion -> {
                            if (amhSaveMessage.currentNarrativeComponent != null) {
                                currentImmersionComponent = amhSaveMessage.currentNarrativeComponent
                            }
                        }
                        AMHSaveMessage.AMHSaveMessageType.PollSelectedProfileAsset -> {
                            if (selectedProfileAsset != null && currentProfileComponent != null && currentImmersionComponent != null) {
                                updateSelectedProfileAssetFromCurrentImmersion()

                                MessageChannel.INTER_MENU_BRIDGE.send(null, MenuDataMessage(ProfileMenuDataParams(null, null, selectedProfileAsset!!.assetInfo(), selectedProfileAsset!!.assetName())))
                            }
                        }
                        AMHSaveMessage.AMHSaveMessageType.PollSelectedNarrativeAsset -> {
                             if (selectedNarrativeAsset != null && currentImmersionComponent != null) {
                                 updateSelectedNarrativeAssetFromCurrentImmersion()

                                 MessageChannel.INTER_MENU_BRIDGE.send(null, MenuDataMessage(null, NarrativeMenuDataParams(null, null, selectedNarrativeAsset!!.assetInfo(), selectedNarrativeAsset!!.assetName())))
                             }
                        }
                        AMHSaveMessage.AMHSaveMessageType.SaveOverwriteProfile -> {
                            if (selectedProfileAsset != null && amhSaveMessage.saveName != null) {

                                selectedProfileAsset!!.profile!!.name = amhSaveMessage.saveName

                                selectedProfileAsset!!.save(selectedProfileAsset!!.newAssetFilename())
                            }
                        }
                        AMHSaveMessage.AMHSaveMessageType.SaveMergeProfile -> {}
                        AMHSaveMessage.AMHSaveMessageType.NewProfile -> {
                            if (amhSaveMessage.saveName != null) {
                                selectedProfileAsset = newProfileAsset(amhSaveMessage.saveName)

                                selectedProfileAsset!!.save()

                                initProfile(selectedProfileAsset!!)

                                Switchboard.noloadNarrative()
                            }
                        }

                        AMHSaveMessage.AMHSaveMessageType.PrepSaveProgress -> {
                            reloadProfileAssets()
                            reloadNarrativeAssets()

                            if (currentProfileComponent != null) {
                                setSelectedProfileAssetById(currentProfileComponent!!.profileId())
                                updateSelectedProfileAssetFromCurrentImmersion()
                            }

                            if (selectedProfileAsset == null) {
                                selectedProfileAsset = newProfileAsset()
                                initProfile(selectedProfileAsset!!)
                            }

                            val profileInfo = selectedProfileAsset!!.assetInfo()

                            if (currentImmersionComponent != null) {
                                setSelectedNarrativeAssetById(currentImmersionComponent!!.narrativeId())
                                setSelectedNarrativeImmersionByIds(selectedProfileAsset!!.assetId(), currentImmersionComponent!!.narrativeId())

                                updateSelectedNarrativeAssetFromCurrentImmersion()
                                updateSelectedImmersionAssetFromCurrentImmersion()
                            }

                            if (selectedNarrativeAsset != null && selectedImmersionAsset == null) {
                                selectedImmersionAsset = NarrativeImmersionAsset(NarrativeImmersion(NarrativeImmersionAsset.genAssetId(selectedProfileAsset!!.assetId(), selectedNarrativeAsset!!.assetId())!!))

                                updateSelectedImmersionAssetFromCurrentImmersion()
                            }

                            val immersionInfo = selectedNarrativeAsset?.assetInfo() ?: listOf(TextViewCtrl.NoNarrativeLoaded)

                            MessageChannel.INTER_MENU_BRIDGE.send(null, MenuDataMessage(ProfileMenuDataParams(null, null, profileInfo), NarrativeMenuDataParams(null, null, immersionInfo)))
                        }

                        AMHSaveMessage.AMHSaveMessageType.SaveProgress -> {
                            selectedProfileAsset!!.save()
                            selectedImmersionAsset?.save()

                            reloadProfileAssets()
                            reloadNarrativeImmersionAssets()
                        }
                    }

                    return true
                }

            }
        }
        return false
    }

    fun newProfileAsset(saveName : String = NameTypes.COMMON.nextName()) : ProfileAsset {

        val newProfileAsset = if (currentProfileComponent != null) {
            ProfileAsset(currentProfileComponent!!.profile).apply {
                this.profile!!.id = Id.randomId()
                this.profile!!.name = saveName
                this.profile!!.cumlTime = ImmersionTimer.zero()
                this.assetPath = this.newAssetFilename()
            }
        } else {
            ProfileAsset(Profile(name=saveName) ).apply {
                this.assetPath = this.newAssetFilename()
            }
        }

        return newProfileAsset
    }

    companion object {
        val json = Json { ignoreUnknownKeys = true }
    }

    enum class SaveType() {
        Merge, Overwrite
    }

    fun dispose() {
        assets.dispose()
    }
}