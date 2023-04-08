package river.exertion.kcop.assets

data class NarrativeImmersionAssets(var values: MutableList<NarrativeImmersionAsset> = mutableListOf()) {

    fun byIds(profileId : String? = null, narrativeId : String? = null) : NarrativeImmersionAsset? =
        values.firstOrNull { it.assetId() == NarrativeImmersionAsset.genAssetId(profileId, narrativeId) }

    companion object {
        val narrativeImmersionAssetLocation = "kcop/immersion/"
        val narrativeImmersionAssetExtension = ".json"
        fun narrativeImmersionAssetPath(narrativeFilename : String) = narrativeImmersionAssetLocation + narrativeFilename + narrativeImmersionAssetExtension
    }
}
