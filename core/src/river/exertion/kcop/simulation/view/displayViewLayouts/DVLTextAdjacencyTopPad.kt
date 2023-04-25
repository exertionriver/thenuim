package river.exertion.kcop.simulation.view.displayViewLayouts

import kotlinx.serialization.Serializable
import river.exertion.kcop.Id

@Serializable
data class DVLTextAdjacencyTopPad(
    var idx : String? = Id.randomId(),
    var fontPads : MutableList<DVLTATPFontPads> = mutableListOf()
)
