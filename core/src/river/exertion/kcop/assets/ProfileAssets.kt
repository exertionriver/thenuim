package river.exertion.kcop.assets

data class ProfileAssets(var values: MutableList<ProfileAsset> = mutableListOf()) {

    fun byId(profileId : String? = null) : ProfileAsset? = values.firstOrNull { it.assetId() == profileId }

    fun byTitle(profileTitle : String? = null) : ProfileAsset? = values.firstOrNull { it.assetTitle() == profileTitle }

    companion object {
        val profileAssetLocation = "kcop/profile/"
        val profileAssetExtension = ".json"
        fun profileAssetPath(profileFilename : String) = profileAssetLocation + profileFilename + profileAssetExtension
    }
}