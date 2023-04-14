package river.exertion.kcop.narrative.structure

import kotlinx.serialization.Serializable
import river.exertion.kcop.narrative.structure.events.Event
import river.exertion.kcop.narrative.structure.events.ITriggerEvent
import river.exertion.kcop.system.immersionTimer.ImmersionTimer

@Serializable
data class TimelineEventBlock(
    val narrativeBlockId : String = "",
    val events : MutableList<Event> = mutableListOf()
) {

    fun readyTimelineBlockEvents(blockCumulativeTimer : ImmersionTimer) : List<Event> {

        return events.filter { timelineEvent ->
            timelineEvent is ITriggerEvent &&
            timelineEvent.timeTrigger() != null &&
            blockCumulativeTimer.onOrPast(timelineEvent.timeTrigger())
        }.sortedBy { timelineEvent ->
            ImmersionTimer.inSeconds((timelineEvent as ITriggerEvent).timeTrigger())
        }
    }
}
