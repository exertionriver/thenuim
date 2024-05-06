package river.exertion.thenuim.view.layout.displayViewLayout

import kotlinx.serialization.Serializable
import river.exertion.thenuim.base.Id

@Serializable
data class DVLTextAdjacencyRow(
    var tag : String? = Id.randomId(),
    var fontRows : MutableList<DVLTARFontRow> = mutableListOf()
)