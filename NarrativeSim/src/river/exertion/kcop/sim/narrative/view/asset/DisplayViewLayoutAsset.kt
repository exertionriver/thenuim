package river.exertion.kcop.sim.narrative.view.asset

import river.exertion.kcop.asset.IAsset
import river.exertion.kcop.asset.IAsset.Companion.AssetNotFound
import river.exertion.kcop.sim.narrative.view.DVLayout

class DisplayViewLayoutAsset(var dvLayout: DVLayout = DVLayout.dvLayout()) : IAsset {

    override fun assetData() : Any = dvLayout

    override var assetId : String = dvLayout.id
    override var assetName : String = dvLayout.name

    override var assetPath : String? = null
    override var assetTitle = assetPath ?: AssetNotFound

    override var status : String? = null
    override var statusDetail : String? = null
    override var persisted = false

    override fun newAssetFilename(): String = ""

    override fun assetInfo() : List<String> = listOf(assetName)

    companion object {
        fun isValid(displayViewLayoutAsset: DisplayViewLayoutAsset?) : Boolean {
            return (displayViewLayoutAsset?.dvLayout != null && displayViewLayoutAsset.status == null)
        }
    }
}