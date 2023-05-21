package river.exertion.kcop.sim.narrative.asset

import river.exertion.kcop.asset.AssetManagerHandler
import river.exertion.kcop.asset.IAsset
import river.exertion.kcop.asset.IAssets
import river.exertion.kcop.profile.asset.ProfileAsset
import river.exertion.kcop.profile.asset.ProfileAssets

object NarrativeAssets : IAssets {

    override var values: MutableList<IAsset> = mutableListOf()

    override fun byId(assetId : String?) : NarrativeAsset? = super.byIdTyped(assetId)

    override fun byTitle(assetTitle : String?) : NarrativeAsset? = super.byTitleTyped(assetTitle)

    override fun byName(assetName : String?) : NarrativeAsset? = super.byNameTyped(assetName)

    override fun <T:IAsset>reloadTyped() : MutableList<T> {
        ProfileAssets.values = AssetManagerHandler.reloadLocalAssets<NarrativeAsset>(iAssetLocation).toMutableList()
        return getTyped()
    }

    override fun reload() : MutableList<NarrativeAsset> = reloadTyped()

    override fun get() : MutableList<NarrativeAsset> = super.getTyped()

    override val iAssetLocation = "kcop/narrative/"
    override val iAssetExtension = ".json"
}