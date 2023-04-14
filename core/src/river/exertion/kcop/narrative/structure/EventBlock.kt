package river.exertion.kcop.narrative.structure

import kotlinx.serialization.Serializable
import river.exertion.kcop.narrative.structure.events.Event

@Serializable
data class EventBlock(
    val narrativeBlockId : String = "",
    val events : MutableList<Event> = mutableListOf()
)
