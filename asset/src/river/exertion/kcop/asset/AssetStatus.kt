package river.exertion.kcop.asset

data class AssetStatus(val assetPath : String, val status : String, var statusDetail : String? = "") {

    companion object {
        const val AssetLoaded = "asset loaded"
        const val AssetNotLoaded = "asset not loaded"
    }
}
