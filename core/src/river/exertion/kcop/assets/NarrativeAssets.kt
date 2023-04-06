package river.exertion.kcop.assets

object NarrativeAssets {
    val narrativeAssetLocation = "kcop/narrative/"
    val narrativeAssetExtension = ".json"
    fun narrativeAssetPath(narrativeFilename : String) = narrativeAssetLocation + narrativeFilename + narrativeAssetExtension
}