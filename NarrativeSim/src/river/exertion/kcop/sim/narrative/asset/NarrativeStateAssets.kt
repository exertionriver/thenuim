package river.exertion.kcop.sim.narrative.asset

import river.exertion.kcop.asset.AssetManagerHandler
import river.exertion.kcop.asset.IAsset
import river.exertion.kcop.asset.IAssets
import river.exertion.kcop.profile.asset.ProfileAsset
import river.exertion.kcop.profile.asset.ProfileAssets

object NarrativeStateAssets : IAssets {

    override var values: MutableList<IAsset> = mutableListOf()

    override fun byId(assetId : String?) : NarrativeStateAsset? = super.byIdTyped(assetId)

    override fun byTitle(assetTitle : String?) : NarrativeStateAsset? = super.byTitleTyped(assetTitle)

    override fun byName(assetName : String?) : NarrativeStateAsset? = super.byNameTyped(assetName)

    override fun <T:IAsset>reloadTyped() : MutableList<T> {
        ProfileAssets.values = AssetManagerHandler.reloadLocalAssets<NarrativeStateAsset>(iAssetLocation).toMutableList()
        return getTyped()
    }

    override fun reload() : MutableList<NarrativeStateAsset> = reloadTyped()

    override fun get() : MutableList<NarrativeStateAsset> = super.getTyped()

    override val iAssetLocation = "kcop/narrativeState/"
    override val iAssetExtension = ".json"
}
