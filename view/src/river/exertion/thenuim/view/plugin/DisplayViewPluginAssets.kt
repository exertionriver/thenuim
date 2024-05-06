package river.exertion.thenuim.view.plugin

import river.exertion.thenuim.asset.AssetManagerHandler
import river.exertion.thenuim.asset.IAsset
import river.exertion.thenuim.asset.IAssets
import river.exertion.thenuim.view.klop.IDisplayViewKlop

object DisplayViewPluginAssets : IAssets {

    override var values: MutableList<IAsset> = mutableListOf()

    override fun byId(assetId : String?) : DisplayViewPluginAsset? = super.byIdTyped(assetId)

    override fun byName(assetName : String?) : DisplayViewPluginAsset? = super.byNameTyped(assetName)

    override fun byTitle(assetTitle : String?) : DisplayViewPluginAsset? = super.byTitleTyped(assetTitle)

    override fun <T: IAsset>reloadTyped() : MutableList<T> {
        val plugins : MutableList<IAsset> = values.plus(
            AssetManagerHandler.reloadLocalAssets<DisplayViewPluginAsset>(
                iAssetsLocation,
                iAssetsExtension
            )
        ).toMutableList()

        values.clear()

        //exclude duplicates, older versions
        plugins.filter { it.assetStatus == null }.sortedByDescending { (it.assetData() as IDisplayViewKlop).version }.sortedBy { (it.assetData() as IDisplayViewKlop).tag }.forEach { candidatePlugin ->
            if (!values.map { plugin -> (plugin.assetData() as IDisplayViewKlop).tag}.contains((candidatePlugin.assetData() as IDisplayViewKlop).tag)) {
                values.add(candidatePlugin)
            }
        }

        values.forEach { (it.assetData() as IDisplayViewKlop).load() }

        return getTyped()
    }

    override fun reload() : MutableList<DisplayViewPluginAsset> = reloadTyped()

    override fun get() : MutableList<DisplayViewPluginAsset> = super.getTyped()

    override val iAssetsLocation = "assetExt/plugin/"
    override val iAssetsExtension = "jar"
}