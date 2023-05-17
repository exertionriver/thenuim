package river.exertion.kcop.sim.narrative.asset

import river.exertion.kcop.asset.IAsset
import river.exertion.kcop.asset.IAssets
import river.exertion.kcop.profile.asset.ProfileAsset

object NarrativeStateAssets : IAssets {

    override var values: MutableList<IAsset> = mutableListOf()

    override fun byId(assetId : String?) : NarrativeStateAsset? = super.byIdTyped(assetId)

    override fun byTitle(assetTitle : String?) : NarrativeStateAsset? = super.byTitleTyped(assetTitle)

    override fun byName(assetName : String?) : NarrativeStateAsset? = super.byNameTyped(assetName)

    override fun reload() : MutableList<NarrativeStateAsset> = super.reloadTyped()

    override fun get() : MutableList<NarrativeStateAsset> = super.getTyped()

    override val iAssetLocation = "kcop/narrativeState/"
    override val iAssetExtension = ".json"
}
