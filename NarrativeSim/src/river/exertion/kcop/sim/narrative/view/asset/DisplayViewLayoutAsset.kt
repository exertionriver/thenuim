package river.exertion.kcop.sim.narrative.view.asset

import river.exertion.kcop.asset.IAsset
import river.exertion.kcop.asset.IAsset.Companion.AssetNotFound
import river.exertion.kcop.sim.narrative.view.DVLayout

class DisplayViewLayoutAsset(var dvLayout: DVLayout? = null) : IAsset {
    override var assetPath : String? = null
    override var status : String? = null
    override var statusDetail : String? = null
    override var persisted = false

    override fun assetId() : String = if (dvLayout != null) dvLayout?.id!! else throw Exception("DisplayViewLayoutAsset::assetId() displayViewLayout is null")
    override fun assetName() : String = if (dvLayout != null) dvLayout?.name!! else throw Exception("DisplayViewLayoutAsset::assetName() displayViewLayout is null")
    override fun assetTitle() = assetPath ?: AssetNotFound

    override fun newAssetFilename(): String = ""

    override fun assetInfo() : List<String> = listOf(assetName())

    companion object {
        fun isValid(displayViewLayoutAsset: DisplayViewLayoutAsset?) : Boolean {
            return (displayViewLayoutAsset?.dvLayout != null && displayViewLayoutAsset.status == null)
        }
    }
}