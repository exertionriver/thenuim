package river.exertion.kcop.assets

import com.badlogic.gdx.assets.AssetManager
import ktx.assets.getAsset
import river.exertion.kcop.narrative.structure.Narrative
import river.exertion.kcop.system.ecs.component.NarrativeComponent

class NarrativeAsset(var narrative : Narrative? = null) : IAsset {
    override lateinit var assetPath : String
    override var status : String? = null
    override var statusDetail : String? = null

    override fun assetId() : String = if (narrative != null) narrative?.id!! else throw Exception("NarrativeAsset::assetId() narrative is null")
    override fun assetName() : String = if (narrative != null) narrative?.name!! else throw Exception("NarrativeAsset::assetName() narrative is null")
    override fun assetTitle() = assetPath

    override fun newAssetFilename(): String = NarrativeAssets.narrativeAssetPath(super.newAssetFilename())

    override fun assetInfo() : List<String> {

        val returnList = mutableListOf<String>()

        if (narrative != null) {
            returnList.add("path: $assetPath")
            returnList.addAll(narrative!!.narrativeInfo())
        } else {
            returnList.add("no narrative info found")
        }

        return returnList.toList()
    }

    companion object {
        operator fun AssetManager.get(asset: NarrativeAsset) = getAsset<NarrativeAsset>(asset.assetPath).also {
            if (it.status != null) println ("Asset Status: ${it.status}")
            if (it.statusDetail != null) println ("Status Detail: ${it.statusDetail}")
        }

        fun isValid(narrativeAsset: NarrativeAsset?) : Boolean {
            return (narrativeAsset?.narrative != null && narrativeAsset.status == null)
        }
    }
}