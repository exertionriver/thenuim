package river.exertion.kcop.assets

import com.badlogic.gdx.assets.AssetManager
import ktx.assets.getAsset
import river.exertion.kcop.simulation.view.displayViewLayouts.DVLayout

class DisplayViewLayoutAsset(var DVLayout: DVLayout? = null) : IAsset {
    override lateinit var assetPath : String
    override var status : String? = null
    override var statusDetail : String? = null

    override fun assetId() : String = if (DVLayout != null) DVLayout?.id!! else throw Exception("DisplayViewLayoutAsset::assetId() displayViewLayout is null")
    override fun assetName() : String = if (DVLayout != null) DVLayout?.name!! else throw Exception("DisplayViewLayoutAsset::assetName() displayViewLayout is null")
    override fun assetTitle() = assetPath

    override fun newAssetFilename(): String = ""

    override fun assetInfo() : List<String> = listOf(assetName())

    companion object {
        operator fun AssetManager.get(asset: DisplayViewLayoutAsset) = getAsset<DisplayViewLayoutAsset>(asset.assetPath).also {
            if (it.status != null) println ("Asset Status: ${it.status}")
            if (it.statusDetail != null) println ("Status Detail: ${it.statusDetail}")
        }

        fun isValid(displayViewLayoutAsset: DisplayViewLayoutAsset?) : Boolean {
            return (displayViewLayoutAsset?.DVLayout != null && displayViewLayoutAsset.status == null)
        }
    }
}