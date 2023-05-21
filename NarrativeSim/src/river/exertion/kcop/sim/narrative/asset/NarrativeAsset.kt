package river.exertion.kcop.sim.narrative.asset

import river.exertion.kcop.asset.IAsset
import river.exertion.kcop.asset.IAsset.Companion.AssetNotFound
import river.exertion.kcop.sim.narrative.structure.Narrative

class NarrativeAsset(var narrative : Narrative = Narrative()) : IAsset {
    override var assetPath : String? = null
    override var status : String? = null
    override var statusDetail : String? = null

    override fun assetId() : String = narrative.id
    override fun assetName() : String = narrative.name
    override fun assetTitle() = assetPath ?: AssetNotFound

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

        fun isNarrativeLoaded() = (currentNarrativeAsset.assetPath != null)

        fun isValid(narrativeAsset: NarrativeAsset?) : Boolean {
            return (narrativeAsset?.narrative != null && narrativeAsset.status == null)
        }
    }
}