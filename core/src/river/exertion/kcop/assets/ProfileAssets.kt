package river.exertion.kcop.assets

object ProfileAssets {
    val profileAssetLocation = "kcop/profile/"
    val profileAssetExtension = ".json"
    fun profileAssetPath(profileFilename : String) = profileAssetLocation + profileFilename + profileAssetExtension
}