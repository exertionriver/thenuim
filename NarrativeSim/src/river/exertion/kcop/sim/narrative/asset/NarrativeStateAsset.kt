package river.exertion.kcop.sim.narrative.asset

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import kotlinx.serialization.json.encodeToJsonElement
import ktx.assets.getAsset
import river.exertion.kcop.asset.AssetManagerHandler.json
import river.exertion.kcop.asset.IAsset
import river.exertion.kcop.plugin.immersionTimer.ImmersionTimer
import river.exertion.kcop.sim.narrative.component.NarrativeComponent
import river.exertion.kcop.sim.narrative.structure.NarrativeState

class NarrativeStateAsset(var narrativeState : NarrativeState = NarrativeState()) : IAsset {

    override lateinit var assetPath : String
    override var status : String? = null
    override var statusDetail : String? = null

    override fun assetId() = narrativeState.id

    override fun assetName() = assetId()

    override fun assetTitle() = assetPath

    override fun newAssetFilename(): String = NarrativeStateAssets.iAssetPath(assetName())

    override fun assetInfo() : List<String> {

        val returnList = mutableListOf<String>()

        returnList.add(narrativeState.cumlImmersionTime() ?: "no narrative immersion info found")

        return returnList.toList()
    }

    fun narrativeCurrBlockId() = narrativeState.immersionBlockId()

    fun cumlImmersionTime() = narrativeState.cumlImmersionTime()

    fun flags() = narrativeState.flags

    fun timers() = narrativeState.blockImmersionTimers

    fun save() {
        assetPath = newAssetFilename()
        val jsonNarrativeImmersion = json.encodeToJsonElement(this.narrativeState)
        Gdx.files.local(assetPath).writeString(jsonNarrativeImmersion.toString(), false)
    }

    companion object {
        var currentNarrativeStateAsset = NarrativeStateAsset()

        operator fun AssetManager.get(asset: NarrativeStateAsset) = getAsset<NarrativeStateAsset>(asset.assetPath).also {
            if (it.status != null) println ("Asset Status: ${it.status}")
            if (it.statusDetail != null) println ("Status Detail: ${it.statusDetail}")
        }

        fun isValid(narrativeStateAsset: NarrativeStateAsset?) : Boolean {
            return (narrativeStateAsset?.narrativeState != null && narrativeStateAsset.status == null)
        }

        fun new(narrativeComponent: NarrativeComponent) : NarrativeStateAsset {
            return NarrativeStateAsset(narrativeComponent.narrativeState).apply {
                this.assetPath = newAssetFilename()
            }
        }
    }
}