package river.exertion.kcop.assets

import com.badlogic.gdx.assets.AssetManager
import ktx.assets.getAsset
import river.exertion.kcop.narrative.structure.Narrative
import river.exertion.kcop.system.ecs.component.NarrativeComponent

class DisplayViewLayoutAsset(var displayViewLayout: DisplayViewLayout? = null) : IAsset {
    override lateinit var assetPath : String
    override var status : String? = null
    override var statusDetail : String? = null

    override fun assetId() : String = if (displayViewLayout != null) displayViewLayout?.id!! else throw Exception("DisplayViewLayoutAsset::assetId() displayViewLayout is null")
    override fun assetName() : String = if (displayViewLayout != null) displayViewLayout?.name!! else throw Exception("DisplayViewLayoutAsset::assetName() displayViewLayout is null")
    override fun assetTitle() = assetPath

    override fun newAssetFilename(): String = ""

    override fun assetInfo() : List<String> = listOf(assetName())

    companion object {
        operator fun AssetManager.get(asset: DisplayViewLayoutAsset) = getAsset<DisplayViewLayoutAsset>(asset.assetPath).also {
            if (it.status != null) println ("Asset Status: ${it.status}")
            if (it.statusDetail != null) println ("Status Detail: ${it.statusDetail}")
        }

        fun isValid(displayViewLayoutAsset: DisplayViewLayoutAsset?) : Boolean {
            return (displayViewLayoutAsset?.displayViewLayout != null && displayViewLayoutAsset.status == null)
        }
    }
}