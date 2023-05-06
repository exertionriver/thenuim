package river.exertion.kcop.sim.narrative.asset

import river.exertion.kcop.asset.AssetManagerHandler

data class NarrativeAssets(var values: MutableList<NarrativeAsset> = mutableListOf()) {

    fun byId(narrativeId : String?) : NarrativeAsset? = values.firstOrNull { it.assetId() == narrativeId }

    fun byTitle(narrativeTitle : String?) : NarrativeAsset? = values.firstOrNull { it.assetTitle() == narrativeTitle }

    fun reload() {
        values = AssetManagerHandler.reloadLocalAssets<NarrativeAsset>(narrativeAssetLocation).toMutableList()
    }

    companion object {
        val narrativeAssetLocation = "kcop/narrative/"
        val narrativeAssetExtension = ".json"
        fun narrativeAssetPath(narrativeFilename : String) = narrativeAssetLocation + narrativeFilename + narrativeAssetExtension
    }
}