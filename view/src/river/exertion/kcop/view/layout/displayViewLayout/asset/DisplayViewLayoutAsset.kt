package river.exertion.kcop.view.layout.displayViewLayout.asset

import river.exertion.kcop.asset.AssetStatus
import river.exertion.kcop.asset.IAsset
import river.exertion.kcop.view.layout.displayViewLayout.DVLayout

class DisplayViewLayoutAsset(var dvLayout: DVLayout = DVLayout.dvLayout()) : IAsset {

    override fun assetData() : Any = dvLayout

    override fun assetId() : String = dvLayout.id
    override fun assetName() : String = dvLayout.name

    override fun assetPath() : String = DisplayViewLayoutAssets.iAssetPath(super.newAssetFilename())
    override fun assetTitle() = assetPath()

    override var assetStatus : AssetStatus? = null

    override var persisted = false

    override fun newAssetFilename(): String = ""

    override fun assetInfo() : List<String> = listOf(assetName())

    companion object {
        fun isValid(displayViewLayoutAsset: DisplayViewLayoutAsset?) : Boolean {
            return (displayViewLayoutAsset?.dvLayout != null && displayViewLayoutAsset.assetStatus == null)
        }
    }
}