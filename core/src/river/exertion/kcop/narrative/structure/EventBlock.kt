package river.exertion.kcop.narrative.structure

import kotlinx.serialization.Serializable

@Serializable
data class EventBlock(
    val narrativeBlockId : String = "",
    val events : MutableList<Event> = mutableListOf()
)
