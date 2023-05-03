package river.exertion.kcop.sim.narrative.asset

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import kotlinx.serialization.json.encodeToJsonElement
import ktx.assets.getAsset
import river.exertion.kcop.asset.AssetManagerHandler.json
import river.exertion.kcop.asset.IAsset
import river.exertion.kcop.ecs.immersionTimer.ImmersionTimer
import river.exertion.kcop.sim.narrative.component.NarrativeComponent
import river.exertion.kcop.sim.narrative.structure.NarrativeImmersion

class NarrativeImmersionAsset(var narrativeImmersion : NarrativeImmersion? = null) : IAsset {
    override lateinit var assetPath : String
    override var status : String? = null
    override var statusDetail : String? = null

    override fun assetId() = if (narrativeImmersion != null) narrativeImmersion?.id!! else throw Exception("NarrativeImmersionAsset::assetId() narrativeImmersion is null")

    override fun assetName() = assetId()

    override fun assetTitle() = assetPath

    override fun newAssetFilename(): String = NarrativeImmersionAssets.narrativeImmersionAssetPath(assetName())

    override fun assetInfo() : List<String> {

        val returnList = mutableListOf<String>()

        returnList.add(narrativeImmersion?.cumlImmersionTime() ?: "no narrative immersion info found")

        return returnList.toList()
    }

    fun narrativeCurrBlockId() = if (narrativeImmersion != null) narrativeImmersion?.immersionBlockId() else NarrativeImmersion.UnknownBlockId
    fun cumlImmersionTime() = if (narrativeImmersion != null) narrativeImmersion?.cumlImmersionTime() else ImmersionTimer.CumlTimeZero

    fun flags() = if (narrativeImmersion != null) narrativeImmersion?.flags else listOf()

    fun timers() = if (narrativeImmersion != null) narrativeImmersion?.blockImmersionTimers else mapOf()

    fun save() {
        assetPath = newAssetFilename()
        val jsonNarrativeImmersion = json.encodeToJsonElement(this.narrativeImmersion)
        Gdx.files.local(assetPath).writeString(jsonNarrativeImmersion.toString(), false)
    }

    //TODO: append data rather than delete file
    fun delete() {
        Gdx.files.local(assetPath).delete()
    }

    fun update(narrativeImmersionComponent : NarrativeComponent?) {
        narrativeImmersion = narrativeImmersionComponent?.narrativeImmersion
        narrativeImmersion?.blockImmersionTimers = narrativeImmersionComponent?.blockImmersionTimersStr() ?: mutableMapOf()
    }

    companion object {
        operator fun AssetManager.get(asset: NarrativeImmersionAsset) = getAsset<NarrativeImmersionAsset>(asset.assetPath).also {
            if (it.status != null) println ("Asset Status: ${it.status}")
            if (it.statusDetail != null) println ("Status Detail: ${it.statusDetail}")
        }

        fun isValid(narrativeImmersionAsset: NarrativeImmersionAsset?) : Boolean {
            return (narrativeImmersionAsset?.narrativeImmersion != null && narrativeImmersionAsset.status == null)
        }

        fun new(narrativeComponent: NarrativeComponent) : NarrativeImmersionAsset {
            return NarrativeImmersionAsset(narrativeComponent.narrativeImmersion).apply {
                this.assetPath = newAssetFilename()
            }
        }
    }
}