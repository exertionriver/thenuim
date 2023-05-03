package river.exertion.kcop.profile.asset

import river.exertion.kcop.asset.AssetManagerHandler.reloadLocalAssets

data class ProfileAssets(var values: MutableList<ProfileAsset> = mutableListOf()) {

    fun byId(profileId : String?) : ProfileAsset? = values.firstOrNull { it.assetId() == profileId }

    fun byTitle(profileTitle : String?) : ProfileAsset? = values.firstOrNull { it.assetTitle() == profileTitle }

    fun reload() {
        values = reloadLocalAssets<ProfileAsset>(profileAssetLocation).toMutableList()
    }

    companion object {
        val profileAssetLocation = "kcop/profile/"
        val profileAssetExtension = ".json"
        fun profileAssetPath(profileFilename : String) = profileAssetLocation + profileFilename + profileAssetExtension
    }
}