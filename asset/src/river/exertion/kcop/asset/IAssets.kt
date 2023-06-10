package river.exertion.kcop.asset

interface IAssets {

    var values: MutableList<IAsset>

    @Suppress("UNCHECKED_CAST")
    fun <T:IAsset>byIdTyped(assetId : String?) : T? = values.firstOrNull { it.assetId() == assetId } as T?

    fun byId(assetId : String?) : Any?

    @Suppress("UNCHECKED_CAST")
    fun <T:IAsset>byNameTyped(assetName : String?) : T? = values.firstOrNull { it.assetName() == assetName } as T?

    fun byName(assetName : String?) : Any?

    @Suppress("UNCHECKED_CAST")
    fun <T:IAsset>byTitleTyped(assetTitle : String?) : T? = values.firstOrNull { it.assetTitle() == assetTitle } as T?

    fun byTitle(assetTitle : String?) : Any?

    //must be overridden, there is no IAsset loader
    fun <T:IAsset>reloadTyped() : MutableList<T> {
        values = AssetManagerHandler.reloadLocalAssets<IAsset>(iAssetsLocation, iAssetsExtension).toMutableList()
        return getTyped()
    }

    fun reload() : MutableList<*>

    @Suppress("UNCHECKED_CAST")
    fun <T:IAsset>getTyped() : MutableList<T> = values as MutableList<T>

    fun get() : MutableList<*>

    val iAssetsLocation : String
    val iAssetsExtension : String
    fun iAssetPath(iAssetFilename : String) = "$iAssetsLocation$iAssetFilename.$iAssetsExtension"
}