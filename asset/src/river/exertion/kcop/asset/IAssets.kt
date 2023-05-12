package river.exertion.kcop.asset

interface IAssets {

    var values: MutableList<IAsset>

    fun byId(assetId : String?) : IAsset? = values.firstOrNull { it.assetId() == assetId }

    fun byTitle(assetTitle : String?) : IAsset? = values.firstOrNull { it.assetTitle() == assetTitle }

    fun reload() : MutableList<IAsset> {
        values = AssetManagerHandler.reloadLocalAssets<IAsset>(iAssetLocation).toMutableList()
        return values
    }

    val iAssetLocation : String
    val iAssetExtension : String

    fun iAssetPath(iAssetFilename : String) = iAssetLocation + iAssetFilename + iAssetExtension
}