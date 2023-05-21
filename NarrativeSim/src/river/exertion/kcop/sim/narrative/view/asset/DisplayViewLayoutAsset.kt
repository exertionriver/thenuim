package river.exertion.kcop.sim.narrative.view.asset

import river.exertion.kcop.asset.IAsset
import river.exertion.kcop.asset.IAsset.Companion.AssetNotFound
import river.exertion.kcop.sim.narrative.view.DVLayout

class DisplayViewLayoutAsset(var DVLayout: DVLayout? = null) : IAsset {
    override var assetPath : String? = null
    override var status : String? = null
    override var statusDetail : String? = null

    override fun assetId() : String = if (DVLayout != null) DVLayout?.id!! else throw Exception("DisplayViewLayoutAsset::assetId() displayViewLayout is null")
    override fun assetName() : String = if (DVLayout != null) DVLayout?.name!! else throw Exception("DisplayViewLayoutAsset::assetName() displayViewLayout is null")
    override fun assetTitle() = assetPath ?: AssetNotFound

    override fun newAssetFilename(): String = ""

    override fun assetInfo() : List<String> = listOf(assetName())

    companion object {
        fun isValid(displayViewLayoutAsset: DisplayViewLayoutAsset?) : Boolean {
            return (displayViewLayoutAsset?.DVLayout != null && displayViewLayoutAsset.status == null)
        }
    }
}