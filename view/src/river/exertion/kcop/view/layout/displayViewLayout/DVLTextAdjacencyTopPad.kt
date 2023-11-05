package river.exertion.kcop.view.layout.displayViewLayout

import kotlinx.serialization.Serializable
import river.exertion.kcop.base.Id

@Serializable
data class DVLTextAdjacencyTopPad(
    var tag : String? = Id.randomId(),
    var fontPads : MutableList<DVLTATPFontPads> = mutableListOf()
)
