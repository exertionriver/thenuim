package river.exertion.kcop.asset

interface IAsset {
    var assetPath : String
    var status : String?
    var statusDetail : String?

    fun assetId() : String
    fun assetName() : String
    fun assetTitle() : String
    fun assetInfo() : List<String>

    fun newAssetFilename() = newAssetFilename(assetName(), assetId())

    companion object {
        val replaceChars = """[.@{}!\\`´"^=()&\[\]$'~#%*:+<>?/|, ]"""
        val reduceUnderscores = """_+"""

        fun replaceFileName(assetName : String) = assetName.replace(replaceChars.toRegex(), "_")

        fun reduceFilename(assetName: String) = assetName.replace(reduceUnderscores.toRegex(), "_")

        fun newAssetFilename(assetName : String, assetId : String) = "${reduceFilename(replaceFileName(assetName))}_${assetId.substring(0, 4)}"
    }
}