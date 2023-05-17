package river.exertion.kcop.sim.narrative.asset

import com.badlogic.gdx.assets.AssetManager
import ktx.assets.getAsset
import river.exertion.kcop.asset.IAsset
import river.exertion.kcop.sim.narrative.structure.Narrative

class NarrativeAsset(var narrative : Narrative = Narrative()) : IAsset {
    override lateinit var assetPath : String
    override var status : String? = null
    override var statusDetail : String? = null

    override fun assetId() : String = narrative.id
    override fun assetName() : String = narrative.name
    override fun assetTitle() = assetPath

    override fun newAssetFilename(): String = NarrativeAssets.iAssetPath(super.newAssetFilename())

    override fun assetInfo() : List<String> {

        val returnList = mutableListOf<String>()

        returnList.add("path: $assetPath")
        returnList.addAll(narrative.narrativeInfo())

        return returnList.toList()
    }

    companion object {
        var selectedNarrativeAsset = NarrativeAsset()
        var currentNarrativeAsset = NarrativeAsset()

        operator fun AssetManager.get(asset: NarrativeAsset) = getAsset<NarrativeAsset>(asset.assetPath).also {
            if (it.status != null) println ("Asset Status: ${it.status}")
            if (it.statusDetail != null) println ("Status Detail: ${it.statusDetail}")
        }

        fun isValid(narrativeAsset: NarrativeAsset?) : Boolean {
            return (narrativeAsset?.narrative != null && narrativeAsset.status == null)
        }
    }
}