package river.exertion.kcop.assets

data class NarrativeAssets(var values: MutableList<NarrativeAsset> = mutableListOf()) {

    fun byId(narrativeId : String? = null) : NarrativeAsset? = values.firstOrNull { it.assetId() == narrativeId }

    fun byTitle(narrativeTitle : String? = null) : NarrativeAsset? = values.firstOrNull { it.assetTitle() == narrativeTitle }

    companion object {
        val narrativeAssetLocation = "kcop/narrative/"
        val narrativeAssetExtension = ".json"
        fun narrativeAssetPath(narrativeFilename : String) = narrativeAssetLocation + narrativeFilename + narrativeAssetExtension
    }
}