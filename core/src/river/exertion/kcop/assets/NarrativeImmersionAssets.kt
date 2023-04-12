package river.exertion.kcop.assets

import river.exertion.kcop.narrative.structure.NarrativeImmersion

data class NarrativeImmersionAssets(var values: MutableList<NarrativeImmersionAsset> = mutableListOf()) {

    fun byIds(profileId : String?, narrativeId : String?) : NarrativeImmersionAsset? = values.firstOrNull {
        it.assetId() == NarrativeImmersion.genId(profileId, narrativeId)
    }

    companion object {
        val narrativeImmersionAssetLocation = "kcop/immersion/"
        val narrativeImmersionAssetExtension = ".json"
        fun narrativeImmersionAssetPath(narrativeFilename : String) = narrativeImmersionAssetLocation + narrativeFilename + narrativeImmersionAssetExtension
    }
}
