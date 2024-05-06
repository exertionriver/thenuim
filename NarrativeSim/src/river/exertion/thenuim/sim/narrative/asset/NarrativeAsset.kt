package river.exertion.thenuim.sim.narrative.asset

import river.exertion.thenuim.asset.AssetStatus
import river.exertion.thenuim.asset.IAsset
import river.exertion.thenuim.sim.narrative.structure.Narrative

class NarrativeAsset(var narrative : Narrative = Narrative()) : IAsset {

    override fun assetData() : Any = narrative

    override fun assetId() : String = narrative.id
    override fun assetName() : String = narrative.name

    override fun assetPath() : String = newAssetFilename()
    override fun assetTitle() = assetPath()

    override var assetStatus : AssetStatus? = null

    override var persisted = false

    override fun newAssetFilename(): String = NarrativeAssets.iAssetPath(super.newAssetFilename())

    override fun assetInfo() : List<String> {

        val returnList = mutableListOf<String>()

        returnList.add("path: ${assetPath()}")
        returnList.addAll(narrative.narrativeInfo())

        return returnList.toList()
    }

    companion object {
        var selectedNarrativeAsset = NarrativeAsset()
        var currentNarrativeAsset = NarrativeAsset()

        fun isNarrativeLoaded() = currentNarrativeAsset.persisted

        fun isValid(narrativeAsset: NarrativeAsset?) : Boolean {
            return (narrativeAsset?.narrative != null && narrativeAsset.assetStatus == null)
        }
    }
}