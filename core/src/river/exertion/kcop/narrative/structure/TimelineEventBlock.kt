package river.exertion.kcop.narrative.structure

import kotlinx.serialization.Serializable

@Serializable
data class TimelineEventBlock(
    val narrativeBlockId : String = "",
    val timelineEvents : MutableList<TimelineEvent> = mutableListOf()
)
