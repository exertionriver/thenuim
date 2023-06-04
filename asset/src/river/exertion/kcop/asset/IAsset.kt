package river.exertion.kcop.asset

interface IAsset {
    fun assetData() : Any

    var assetId : String
    var assetName : String

    var assetPath : String?
    var assetTitle : String

    var status : String?
    var statusDetail : String?
    var persisted : Boolean

    fun assetInfo() : List<String>
    fun assetInfoStr() = assetInfo().reduceOrNull { acc, s -> acc + "\n$s"} ?: ""

    fun newAssetFilename() = newAssetFilename(assetName, assetId)

    //must be overridden, there is no IAsset loader
    fun saveTyped(assetSaveLocation : String? = null) {
        persisted = AssetManagerHandler.saveAsset<IAsset>(this, assetSaveLocation).persisted
    }

    fun save(assetSaveLocation : String? = null) {
        this.saveTyped(assetSaveLocation)
    }

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