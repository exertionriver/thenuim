package river.exertion.kcop.sim.narrative.asset

import river.exertion.kcop.asset.AssetManagerHandler
import river.exertion.kcop.asset.IAsset
import river.exertion.kcop.asset.IAssets

object NarrativeAssets : IAssets {

    override var values: MutableList<IAsset> = mutableListOf()

    override fun byId(assetId : String?) : IAsset? = values.firstOrNull { it.assetId() == assetId }

    override fun byTitle(assetTitle : String?) : IAsset? = values.firstOrNull { it.assetTitle() == assetTitle }

    override fun reload() : MutableList<IAsset> {
        values = AssetManagerHandler.reloadLocalAssets<NarrativeAsset>(iAssetLocation).toMutableList()
        return values
    }

    override val iAssetLocation = "kcop/narrative/"
    override val iAssetExtension = ".json"
}