package river.exertion.kcop.sim.narrative.view.asset

import river.exertion.kcop.asset.IAsset
import river.exertion.kcop.asset.IAssets
import river.exertion.kcop.sim.narrative.asset.NarrativeStateAsset

object DisplayViewLayoutAssets : IAssets {

    override var values: MutableList<IAsset> = mutableListOf()

    override fun byId(assetId : String?) : DisplayViewLayoutAsset? = super.byIdTyped(assetId)

    override fun byTitle(assetTitle : String?) : DisplayViewLayoutAsset? = super.byTitleTyped(assetTitle)

    override fun byName(assetName : String?) : DisplayViewLayoutAsset? = super.byNameTyped(assetName)

    override fun reload() : MutableList<DisplayViewLayoutAsset> = super.reloadTyped()

    override fun get() : MutableList<DisplayViewLayoutAsset> = super.getTyped()

    override val iAssetLocation = "kcop/layout/"
    override val iAssetExtension = ".json"
}
