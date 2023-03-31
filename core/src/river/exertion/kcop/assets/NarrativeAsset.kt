package river.exertion.kcop.assets

import river.exertion.kcop.narrative.structure.Narrative

class NarrativeAsset(var narrative : Narrative? = null, override var assetPath : String) : LoadableAsset {
    override var status : String? = null
    override var statusDetail : String? = null

    fun narrativeAssetTitle() = assetPath

    fun narrativeAssetName() = narrative?.name

    fun narrativeAssetInfo() : List<String> {

        val returnList = mutableListOf<String>()

        if (narrative != null) {
            returnList.add("name: ${narrative!!.name}")
            returnList.add("description: ${narrative!!.description}")
            returnList.add("path: ${assetPath}")
            returnList.add("blocks: ${narrative!!.narrativeBlocks.size}")
        }

        if ( returnList.isEmpty() ) returnList.add("no narrative info found")

        return returnList.toList()
    }
}