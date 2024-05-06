package river.exertion.thenuim.view.layout.displayViewLayout

import kotlinx.serialization.Serializable
import river.exertion.thenuim.base.Id

@Serializable
data class DVLTextAdjacencyTopPad(
    var tag : String? = Id.randomId(),
    var fontPads : MutableList<DVLTATPFontPads> = mutableListOf()
)
