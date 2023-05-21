package river.exertion.kcop.sim.narrative.asset

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import kotlinx.serialization.json.encodeToJsonElement
import ktx.assets.getAsset
import river.exertion.kcop.asset.AssetManagerHandler.json
import river.exertion.kcop.asset.IAsset
import river.exertion.kcop.plugin.immersionTimer.ImmersionTimer
import river.exertion.kcop.profile.asset.ProfileAsset
import river.exertion.kcop.profile.settings.ProfileSettingEntry
import river.exertion.kcop.sim.narrative.asset.NarrativeAsset.Companion.isNarrativeLoaded
import river.exertion.kcop.sim.narrative.component.NarrativeComponent
import river.exertion.kcop.sim.narrative.structure.NarrativeState

class NarrativeStateAsset(var narrativeState : NarrativeState = NarrativeState()) : IAsset {

    override var assetPath : String? = null
    override var status : String? = null
    override var statusDetail : String? = null

    override fun assetId() = narrativeState.id

    override fun assetName() = assetId()

    override fun assetTitle() = assetPath ?: IAsset.AssetNotFound

    override fun newAssetFilename(): String {
        narrativeState.id = NarrativeState.genId(ProfileAsset.currentProfileAsset.assetId(), NarrativeAsset.currentNarrativeAsset.assetId())
        return NarrativeStateAssets.iAssetPath(assetId())
    }

    var cumlImmersionTimer : ImmersionTimer
        get() = narrativeState.cumlImmersionTimer
        set(value) { narrativeState.cumlImmersionTimer = value }

    var blockCumlImmersionTimers : MutableMap<String, ImmersionTimer>
        get() = narrativeState.blockCumlImmersionTimers
        set(value) { narrativeState.blockCumlImmersionTimers = value }

    override fun assetInfo() : List<String> {

        val returnList = mutableListOf<String>()

        returnList.add(narrativeState.cumlImmersionTimer.immersionTime())

        return returnList.toList()
    }

    fun save() {
        if (isNarrativeLoaded()) {
            //used to update serializable fields
            cumlImmersionTimer = cumlImmersionTimer
            blockCumlImmersionTimers = blockCumlImmersionTimers

            if (assetPath == null) assetPath = newAssetFilename()

            val jsonNarrativeImmersion = json.encodeToJsonElement(this.narrativeState)
            Gdx.files.local(assetPath).writeString(jsonNarrativeImmersion.toString(), false)
        }
    }

    companion object {
        var currentNarrativeStateAsset = NarrativeStateAsset()

        fun new(narrativeComponent: NarrativeComponent) : NarrativeStateAsset {
            return NarrativeStateAsset(narrativeComponent.narrativeState).apply {
                this.assetPath = newAssetFilename()
            }
        }
    }
}