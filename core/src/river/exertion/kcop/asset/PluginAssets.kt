package river.exertion.kcop.asset

object PluginAssets : IAssets {

    override var values: MutableList<IAsset> = mutableListOf()

    override fun byId(assetId : String?) : PluginAsset? = super.byIdTyped(assetId)

    override fun byName(assetName : String?) : PluginAsset? = super.byNameTyped(assetName)

    override fun byTitle(assetTitle : String?) : PluginAsset? = super.byTitleTyped(assetTitle)

    override fun <T:IAsset>reloadTyped() : MutableList<T> {
        values = values.plus(AssetManagerHandler.reloadLocalAssets<PluginAsset>(iAssetsLocation, iAssetsExtension)).toMutableList()
        return getTyped()
    }

    override fun reload() : MutableList<PluginAsset> = reloadTyped()

    override fun get() : MutableList<PluginAsset> = super.getTyped()

    override val iAssetsLocation = "plugin/"
    override val iAssetsExtension = "jar"
}