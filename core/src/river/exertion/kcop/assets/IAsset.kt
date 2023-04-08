package river.exertion.kcop.assets

interface IAsset {
    var assetPath : String
    var status : String?
    var statusDetail : String?

    fun assetId() : String
    fun assetName() : String
    fun assetTitle() : String
    fun assetInfo() : List<String>

    fun newAssetFilename() = "${assetName()}_${assetId().substring(0, 4)}"
}