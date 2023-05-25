package river.exertion.kcop.sim.narrative.view

import kotlinx.serialization.Serializable
import river.exertion.kcop.messaging.Id

@Serializable
data class DVLTextAdjacencyTopPad(
    var idx : String? = Id.randomId(),
    var fontPads : MutableList<DVLTATPFontPads> = mutableListOf()
)