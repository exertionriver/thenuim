package river.exertion.kcop.sim.narrative.view

import kotlinx.serialization.Serializable
import river.exertion.kcop.base.Id

@Serializable
data class DVLTextAdjacencyRow(
    var idx : String? = Id.randomId(),
    var fontRows : MutableList<DVLTARFontRow> = mutableListOf()
)