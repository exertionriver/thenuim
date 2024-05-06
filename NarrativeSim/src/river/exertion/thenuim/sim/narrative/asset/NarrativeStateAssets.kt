package river.exertion.thenuim.sim.narrative.asset

import river.exertion.thenuim.asset.AssetManagerHandler
import river.exertion.thenuim.asset.IAsset
import river.exertion.thenuim.asset.IAssets

object NarrativeStateAssets : IAssets {

    override var values: MutableList<IAsset> = mutableListOf()

    override fun byId(assetId : String?) : NarrativeStateAsset? = super.byIdTyped(assetId)

    override fun byName(assetName : String?) : NarrativeStateAsset? = super.byNameTyped(assetName)

    override fun byTitle(assetTitle : String?) : NarrativeStateAsset? = super.byTitleTyped(assetTitle)

    override fun <T: IAsset>reloadTyped() : MutableList<T> {
        values = AssetManagerHandler.reloadLocalAssets<NarrativeStateAsset>(iAssetsLocation, iAssetsExtension).toMutableList()
        return getTyped()
    }

    override fun reload() : MutableList<NarrativeStateAsset> = reloadTyped()

    override fun get() : MutableList<NarrativeStateAsset> = super.getTyped()

    override val iAssetsLocation = "assetExt/narrativeState/"
    override val iAssetsExtension = "json"
}
