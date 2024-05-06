package river.exertion.thenuim.asset

interface IAsset {
    fun assetData() : Any?

    fun assetId() : String
    fun assetName() : String

    fun assetPath() : String
    fun assetTitle() : String

    var assetStatus : AssetStatus?

    var persisted : Boolean

    fun assetInfo() : List<String>

    fun newAssetFilename() = newAssetFilename(assetName(), assetId())

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

        private fun replaceFilename(assetName : String) = assetName.replace(replaceChars.toRegex(), "_")

        private fun reduceFilename(assetName: String) = assetName.replace(reduceUnderscores.toRegex(), "_")

        fun sanitizeFilename(assetName : String) = reduceFilename(replaceFilename(assetName))

        fun newAssetFilename(assetName : String, assetId : String) = "${sanitizeFilename(assetName)}_${assetId.substring(0, assetId.length.coerceIn(0, 4))}"

        const val AssetNotFound = "not found"
    }
}