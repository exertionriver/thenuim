package river.exertion.kcop.view.layout.displayViewLayout.asset

import river.exertion.kcop.asset.AssetManagerHandler
import river.exertion.kcop.asset.IAsset
import river.exertion.kcop.asset.IAssets

object DisplayViewLayoutAssets : IAssets {

    override var values: MutableList<IAsset> = mutableListOf()

    override fun byId(assetId : String?) : DisplayViewLayoutAsset? = super.byIdTyped(assetId)

    override fun byName(assetName : String?) : DisplayViewLayoutAsset? = super.byNameTyped(assetName)

    override fun byTitle(assetTitle : String?) : DisplayViewLayoutAsset? = super.byTitleTyped(assetTitle)

    override fun <T:IAsset>reloadTyped() : MutableList<T> {
        values = AssetManagerHandler.reloadLocalAssets<DisplayViewLayoutAsset>(iAssetsLocation, iAssetsExtension).toMutableList()
        return getTyped()
    }

    override fun reload() : MutableList<DisplayViewLayoutAsset> = reloadTyped()

    override fun get() : MutableList<DisplayViewLayoutAsset> = super.getTyped()

    override val iAssetsLocation = "assetExt/layout/"
    override val iAssetsExtension = "json"
}
