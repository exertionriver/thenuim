package river.exertion.kcop.profile.asset

import river.exertion.kcop.asset.AssetManagerHandler.reloadLocalAssets
import river.exertion.kcop.asset.IAsset
import river.exertion.kcop.asset.IAssets

object ProfileAssets : IAssets {

    override var values: MutableList<IAsset> = mutableListOf()

    override fun byId(assetId : String?) : IAsset? = values.firstOrNull { it.assetId() == assetId }

    override fun byTitle(assetTitle : String?) : IAsset? = values.firstOrNull { it.assetTitle() == assetTitle }

    override fun reload() : MutableList<IAsset> {
        values = reloadLocalAssets<ProfileAsset>(iAssetLocation).toMutableList()
        return values
    }

    override val iAssetLocation = "kcop/profile/"
    override val iAssetExtension = ".json"
}