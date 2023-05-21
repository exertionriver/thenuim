package river.exertion.kcop.sim.narrative.view.asset

import river.exertion.kcop.asset.AssetManagerHandler
import river.exertion.kcop.asset.IAsset
import river.exertion.kcop.asset.IAssets
import river.exertion.kcop.sim.narrative.asset.NarrativeAsset
import river.exertion.kcop.sim.narrative.asset.NarrativeAssets
import river.exertion.kcop.sim.narrative.asset.NarrativeStateAsset

object DisplayViewLayoutAssets : IAssets {

    override var values: MutableList<IAsset> = mutableListOf()

    override fun byId(assetId : String?) : DisplayViewLayoutAsset? = super.byIdTyped(assetId)

    override fun byTitle(assetTitle : String?) : DisplayViewLayoutAsset? = super.byTitleTyped(assetTitle)

    override fun byName(assetName : String?) : DisplayViewLayoutAsset? = super.byNameTyped(assetName)

    override fun <T:IAsset>reloadTyped() : MutableList<T> {
        values = AssetManagerHandler.reloadLocalAssets<DisplayViewLayoutAsset>(iAssetLocation).toMutableList()
        return getTyped()
    }

    override fun reload() : MutableList<NarrativeAsset> = reloadTyped()

    override fun get() : MutableList<DisplayViewLayoutAsset> = super.getTyped()

    override val iAssetLocation = "kcop/layout/"
    override val iAssetExtension = ".json"
}
