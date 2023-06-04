package river.exertion.kcop.sim.narrative.asset

import river.exertion.kcop.asset.IAsset
import river.exertion.kcop.asset.IAsset.Companion.AssetNotFound
import river.exertion.kcop.sim.narrative.structure.Narrative

class NarrativeAsset(var narrative : Narrative = Narrative()) : IAsset {

    override fun assetData() : Any = narrative

    override var assetId : String = narrative.id
    override var assetName : String = narrative.name

    override var assetPath : String? = null
    override var assetTitle = assetPath ?: AssetNotFound

    override var status : String? = null
    override var statusDetail : String? = null
    override var persisted = false


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