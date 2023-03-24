package river.exertion.kcop.narrative.structure

import kotlinx.serialization.Serializable
import river.exertion.kcop.system.immersionTimer.ImmersionTimer

@Serializable
data class TimelineEventBlock(
    val narrativeBlockId : String = "",
    val timelineEvents : MutableList<TimelineEvent> = mutableListOf()
) {

    fun readyTimelineBlockEvents(blockCumulativeTimer : ImmersionTimer) : List<TimelineEvent> {

        return timelineEvents.filter {
            blockCumulativeTimer.onOrPast(it.immersionTime)
        }.sortedBy {
            ImmersionTimer.inSeconds(it.immersionTime)
        }
    }
}
