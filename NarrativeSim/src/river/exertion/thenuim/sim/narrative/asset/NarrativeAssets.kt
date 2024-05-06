package river.exertion.thenuim.sim.narrative.asset

import river.exertion.thenuim.asset.AssetManagerHandler
import river.exertion.thenuim.asset.IAsset
import river.exertion.thenuim.asset.IAssets

object NarrativeAssets : IAssets {

    override var values: MutableList<IAsset> = mutableListOf()

    override fun byId(assetId : String?) : NarrativeAsset? = super.byIdTyped(assetId)

    override fun byName(assetName : String?) : NarrativeAsset? = super.byNameTyped(assetName)

    override fun byTitle(assetTitle : String?) : NarrativeAsset? = super.byTitleTyped(assetTitle)

    override fun <T: IAsset>reloadTyped() : MutableList<T> {
        values = AssetManagerHandler.reloadLocalAssets<NarrativeAsset>(iAssetsLocation, iAssetsExtension).toMutableList()
        return getTyped()
    }

    override fun reload() : MutableList<NarrativeAsset> = reloadTyped()

    override fun get() : MutableList<NarrativeAsset> = super.getTyped()

    override val iAssetsLocation = "assetExt/narrative/"
    override val iAssetsExtension = "json"
}