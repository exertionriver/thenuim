package river.exertion.kcop.sim.narrative.asset

import river.exertion.kcop.asset.AssetManagerHandler
import river.exertion.kcop.asset.IAsset
import river.exertion.kcop.asset.IAssets

object NarrativeStateAssets : IAssets {

    override var values: MutableList<IAsset> = mutableListOf()

    override fun byId(assetId : String?) : NarrativeStateAsset? = super.byIdTyped(assetId)

    override fun byName(assetName : String?) : NarrativeStateAsset? = super.byNameTyped(assetName)

    override fun byTitle(assetTitle : String?) : NarrativeStateAsset? = super.byTitleTyped(assetTitle)

    override fun <T:IAsset>reloadTyped() : MutableList<T> {
        values = AssetManagerHandler.reloadLocalAssets<NarrativeStateAsset>(iAssetLocation).toMutableList()
        return getTyped()
    }

    override fun reload() : MutableList<NarrativeStateAsset> = reloadTyped()

    override fun get() : MutableList<NarrativeStateAsset> = super.getTyped()

    override val iAssetLocation = "kcop/narrativeState/"
    override val iAssetExtension = ".json"
}
