package river.exertion.thenuim.sim.narrative.structure

import kotlinx.serialization.Serializable
import river.exertion.thenuim.sim.narrative.structure.events.Event

@Serializable
data class EventBlock(
    val narrativeBlockId : String = "",
    val events : MutableList<Event> = mutableListOf()
)
