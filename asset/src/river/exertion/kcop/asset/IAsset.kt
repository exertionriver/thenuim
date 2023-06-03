package river.exertion.kcop.asset

interface IAsset {
    var assetPath : String?
    var status : String?
    var statusDetail : String?
    var persisted : Boolean

    fun assetId() : String
    fun assetName() : String
    fun assetTitle() : String
    fun assetInfo() : List<String>

    fun newAssetFilename() = newAssetFilename(assetName(), assetId())

    companion object {
        val replaceChars = """[.@{}!\\`´"^=()&\[\]$'~#%*:+<>?/|, /\r/\n/\t]"""
        val reduceUnderscores = """_+"""

        private fun replaceFileName(assetName : String) = assetName.replace(replaceChars.toRegex(), "_")

        private fun reduceFilename(assetName: String) = assetName.replace(reduceUnderscores.toRegex(), "_")

        fun sanitizeFilename(assetName : String) = reduceFilename(replaceFileName(assetName))

        fun newAssetFilename(assetName : String, assetId : String) = "${sanitizeFilename(assetName)}_${assetId.substring(0, 4)}"

        const val AssetNotFound = "not found"
    }
}