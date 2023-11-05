package river.exertion.kcop.view.layout.displayViewLayout

import kotlinx.serialization.Serializable
import river.exertion.kcop.base.Id

@Serializable
data class DVLTextAdjacencyRow(
    var tag : String? = Id.randomId(),
    var fontRows : MutableList<DVLTARFontRow> = mutableListOf()
)