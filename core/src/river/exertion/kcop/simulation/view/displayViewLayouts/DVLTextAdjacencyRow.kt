package river.exertion.kcop.simulation.view.displayViewLayouts

import kotlinx.serialization.Serializable
import river.exertion.kcop.Id

@Serializable
data class DVLTextAdjacencyRow(
    var idx : String? = Id.randomId(),
    var fontRows : MutableList<DVLTARFontRow> = mutableListOf()
)