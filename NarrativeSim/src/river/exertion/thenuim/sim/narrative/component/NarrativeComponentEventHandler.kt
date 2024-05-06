package river.exertion.thenuim.sim.narrative.component

import river.exertion.thenuim.asset.immersionTimer.ImmersionTimer
import river.exertion.thenuim.sim.narrative.structure.events.Event
import river.exertion.thenuim.sim.narrative.structure.events.HintTextEvent
import river.exertion.thenuim.sim.narrative.structure.events.ITriggerEvent
import river.exertion.thenuim.sim.narrative.structure.events.ReportTextEvent
import river.exertion.thenuim.view.layout.AiView

object NarrativeComponentEventHandler {

    fun NarrativeComponent.currentText() : String {

        var returnText = ""

        if (isInitialized) {
            returnText += narrative.currentText()

            readyCurrentBlockEvents(blockCumlImmersionTimer()).filter { event ->
                (event is ReportTextEvent) &&
                narrativeState.persistEventFired(event.id!!)
            }.sortedBy {
                it.id
            }.forEach { event ->
                returnText += "\n${(event as ReportTextEvent).report}"
            }

            readyCurrentBlockEvents(blockCumlImmersionTimer()).filter { event ->
                (event is HintTextEvent) && narrativeState.persistEventFired(event.id!!)
            }.sortedBy {
                it.id
            }.forEach { event ->
                AiView.addHint(event.id!!, (event as HintTextEvent).report)
            }

            readyTimelineEvents(cumlImmersionTimer).filter { timelineEvent ->
                (timelineEvent is ReportTextEvent) && narrativeState.persistEventFired(timelineEvent.id!!)
            }.sortedBy {
                it.id
            }.forEach { timelineEvent ->
                returnText += "\n${(timelineEvent as ReportTextEvent).report}"
            }

            readyTimelineEvents(cumlImmersionTimer).filter { timelineEvent ->
                (timelineEvent is HintTextEvent) && narrativeState.persistEventFired(timelineEvent.id!!)
            }.sortedBy {
                it.id
            }.forEach { timelineEvent ->
                AiView.addHint(timelineEvent.id!!, (timelineEvent as HintTextEvent).report)
            }

        }

        return returnText
    }

    //outputs aggregate text
    fun NarrativeComponent.executeReadyTimelineEvents() {

        if (isInitialized) {

            readyTimelineEvents(cumlImmersionTimer).filter { timelineEvent ->
                !narrativeState.persistEventFired(timelineEvent.id!!)
            }.forEach { timelineEvent ->
                timelineEvent.execEvent()
            }
        }
    }

    private fun NarrativeComponent.readyTimelineEvents(narrativeCumulativeTimer : ImmersionTimer) : List<Event> {

        if (isInitialized) {
            val narrativeTimelineEvents = narrative.timelineEvents.filter { timelineEvent ->
                (timelineEvent is ITriggerEvent) &&
                timelineEvent.timeTrigger() != null &&
                narrativeCumulativeTimer.onOrPast(timelineEvent.timeTrigger())
            }.sortedBy {timelineEvent ->
                ImmersionTimer.inSeconds((timelineEvent as ITriggerEvent).timeTrigger())
            }

            return narrativeTimelineEvents
        }

        return emptyList()
    }

    @Suppress("NewApi")
    fun NarrativeComponent.executeReadyBlockEvents() {

        if (isInitialized) {

            val previousBlockEvents = readyPreviousBlockEvents()
            val currentBlockEvents = readyCurrentBlockEvents(blockCumlImmersionTimer())

            previousBlockEvents.forEach { previousBlockEvent ->
                if (!narrativeState.persistEventFired(previousBlockEvent.id!!) && !narrativeState.blockEventFired("resolve_${previousBlockEvent.id!!}" ) )
                    previousBlockEvent.resolveEvent(currentBlockEvents.firstOrNull {
                        it.isLikeEvent(previousBlockEvent)
                    })
            }

            currentBlockEvents.forEach { currentBlockEvent ->
                if (!narrativeState.persistEventFired(currentBlockEvent.id!!) && !narrativeState.blockEventFired("exec_${currentBlockEvent.id!!}" ) )
                    currentBlockEvent.execEvent(previousBlockEvents.firstOrNull {
                        it.isLikeEvent(currentBlockEvent)
                    })
            }
        }
    }

    fun NarrativeComponent.currentBlockTimer() : String {
        return if (isInitialized) "\nblock inst time:[${blockInstImmersionTimer().immersionTime()}]\nblock cuml time:[${blockCumlImmersionTimer().immersionTime()}]"
        else ""
    }

    private fun NarrativeComponent.readyPreviousBlockEvents() : MutableList<Event> {

        val previousBlockEvents = mutableListOf<Event>()

        if (isInitialized) {
            narrative.previousEventBlock()?.events?.filter {
                it.isImageEvent()
            }.let { previousBlockEvents.addAll(it ?: emptyList()) }

            narrative.previousEventBlock()?.events?.filter {
                it.isMusicEvent()
            }.let { previousBlockEvents.addAll(it ?: emptyList()) }
        }

        return previousBlockEvents
    }

    private fun NarrativeComponent.readyCurrentBlockEvents(blockCumulativeTimer : ImmersionTimer) : MutableList<Event> {

        val currentBlockEvents = mutableListOf<Event>()

        if (isInitialized) {
            narrative.previousEventBlock()?.events?.filter {
                (it is ITriggerEvent) &&
                (it as ITriggerEvent).blockTrigger() == ITriggerEvent.EventTrigger.ON_EXIT
            }.let { currentBlockEvents.addAll(it ?: emptyList()) }

            narrative.currentEventBlock()?.events?.filter {
                (it is ITriggerEvent) &&
                (it as ITriggerEvent).blockTrigger() == ITriggerEvent.EventTrigger.ON_ENTRY
            }.let { currentBlockEvents.addAll(it ?: emptyList()) }

            narrative.currentEventBlock()?.events?.filter {
                (it is ITriggerEvent) &&
                (it.timeTrigger() != null) &&
                blockCumulativeTimer.onOrPast(it.timeTrigger())
            }.let { currentBlockEvents.addAll(it ?: emptyList()) }

            narrative.currentEventBlock()?.events?.filter {
                (it !is ITriggerEvent)
            }.let { currentBlockEvents.addAll(it ?: emptyList()) }
        }

        return currentBlockEvents
    }
}