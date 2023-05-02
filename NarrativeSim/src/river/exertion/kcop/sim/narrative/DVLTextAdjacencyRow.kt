package river.exertion.kcop.sim.narrative

import kotlinx.serialization.Serializable
import river.exertion.kcop.messaging.Id

@Serializable
data class DVLTextAdjacencyRow(
    var idx : String? = Id.randomId(),
    var fontRows : MutableList<DVLTARFontRow> = mutableListOf()
)