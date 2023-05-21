package river.exertion.kcop.profile.asset

import river.exertion.kcop.asset.AssetManagerHandler
import river.exertion.kcop.asset.IAsset
import river.exertion.kcop.asset.IAssets

object ProfileAssets : IAssets {

    override var values: MutableList<IAsset> = mutableListOf()

    override fun byId(assetId : String?) : ProfileAsset? = super.byIdTyped(assetId)

    override fun byTitle(assetTitle : String?) : ProfileAsset? = super.byTitleTyped(assetTitle)

    override fun byName(assetName : String?) : ProfileAsset? = super.byNameTyped(assetName)

    override fun <T:IAsset>reloadTyped() : MutableList<T> {
        values = AssetManagerHandler.reloadLocalAssets<ProfileAsset>(iAssetLocation).toMutableList()
        return getTyped()
    }

    override fun reload() : MutableList<ProfileAsset> = reloadTyped()

    override fun get() : MutableList<ProfileAsset> = super.getTyped()

    override val iAssetLocation = "kcop/profile/"
    override val iAssetExtension = ".json"
}