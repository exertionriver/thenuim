package river.exertion.kcop.assets

import river.exertion.kcop.narrative.structure.Narrative

class NarrativeAsset(var narrative : Narrative? = null) : LoadableAsset {
    override var status : String? = null
    override var statusDetail : String? = null
    override var assetPath : String? = null
}