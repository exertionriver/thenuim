package river.exertion.kcop.sim.narrative.view.asset

import river.exertion.kcop.asset.AssetManagerHandler
import river.exertion.kcop.asset.IAsset
import river.exertion.kcop.asset.IAssets

object DisplayViewLayoutAssets : IAssets {

    override var values: MutableList<IAsset> = mutableListOf()

    override fun byId(assetId : String?) : IAsset? = values.firstOrNull { it.assetId() == assetId }

    fun byName(assetName : String?) : IAsset? = values.firstOrNull { it.assetName() == assetName }

    override fun byTitle(assetTitle : String?) : IAsset? = values.firstOrNull { it.assetTitle() == assetTitle }

    override fun reload() : MutableList<IAsset> {
        values = AssetManagerHandler.reloadLocalAssets<DisplayViewLayoutAsset>(iAssetLocation).toMutableList()
        return values
    }

    override val iAssetLocation = "kcop/layout/"
    override val iAssetExtension = ".json"
}
