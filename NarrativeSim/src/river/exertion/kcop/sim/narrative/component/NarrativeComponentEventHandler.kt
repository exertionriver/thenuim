package river.exertion.kcop.sim.narrative.component

import river.exertion.kcop.plugin.immersionTimer.ImmersionTimer
import river.exertion.kcop.sim.narrative.structure.events.Event
import river.exertion.kcop.sim.narrative.structure.events.HintTextEvent
import river.exertion.kcop.sim.narrative.structure.events.ITriggerEvent
import river.exertion.kcop.sim.narrative.structure.events.ReportTextEvent
import river.exertion.kcop.view.layout.AiView

object NarrativeComponentEventHandler {

    fun NarrativeComponent.currentText() : String {

        var returnText = ""

        if (isInitialized) {
            returnText += narrative!!.currentText()

            readyCurrentBlockEvents(cumlBlockImmersionTimer()).filter { event ->
                (event is ReportTextEvent) &&
                narrativeImmersion!!.persistEventFired(event.id!!)
            }.sortedBy {
                it.id
            }.forEach { event ->
                returnText += "\n${(event as ReportTextEvent).report}"
            }

            readyCurrentBlockEvents(cumlBlockImmersionTimer()).filter { event ->
                (event is HintTextEvent) && narrativeImmersion!!.persistEventFired(event.id!!)
            }.sortedBy {
                it.id
            }.forEach { event ->
                AiView.addHint(event.id!!, (event as HintTextEvent).report)
            }

            readyTimelineEvents(timerPair.cumlImmersionTimer).filter { timelineEvent ->
                (timelineEvent is ReportTextEvent) && narrativeImmersion!!.persistEventFired(timelineEvent.id!!)
            }.sortedBy {
                it.id
            }.forEach { timelineEvent ->
                returnText += "\n${(timelineEvent as ReportTextEvent).report}"
            }

            readyTimelineEvents(timerPair.cumlImmersionTimer).filter { timelineEvent ->
                (timelineEvent is HintTextEvent) && narrativeImmersion!!.persistEventFired(timelineEvent.id!!)
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

            readyTimelineEvents(timerPair.cumlImmersionTimer).filter { timelineEvent ->
                !narrativeImmersion!!.persistEventFired(timelineEvent.id!!)
            }.forEach { timelineEvent ->
                timelineEvent.execEvent()
            }
        }
    }

    private fun NarrativeComponent.readyTimelineEvents(narrativeCumulativeTimer : ImmersionTimer) : List<Event> {

        if (isInitialized) {
            val narrativeTimelineEvents = narrative!!.timelineEvents.filter { timelineEvent ->
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
            val currentBlockEvents = readyCurrentBlockEvents(cumlBlockImmersionTimer())

            previousBlockEvents.forEach { previousBlockEvent ->
                if (!narrativeImmersion!!.persistEventFired(previousBlockEvent.id!!) && !eventFired("resolve_${previousBlockEvent.id!!}" ) )
                    previousBlockEvent.resolveEvent(currentBlockEvents.firstOrNull {
                        it.isLikeEvent(previousBlockEvent)
                    })
            }

            currentBlockEvents.forEach { currentBlockEvent ->
                if (!narrativeImmersion!!.persistEventFired(currentBlockEvent.id!!) && !eventFired("exec_${currentBlockEvent.id!!}" ) )
                    currentBlockEvent.execEvent(previousBlockEvents.firstOrNull {
                        it.isLikeEvent(currentBlockEvent)
                    })
            }
        }
    }

    fun NarrativeComponent.currentBlockTimer() : String {
        return if (isInitialized) "\nblock inst time:[${blockImmersionTimers[narrativeCurrBlockId()]?.instImmersionTimer?.immersionTime()}]\nblock cuml time:[${blockImmersionTimers[narrativeCurrBlockId()]?.cumlImmersionTimer?.immersionTime()}]"
        else ""
    }

    private fun NarrativeComponent.readyPreviousBlockEvents() : MutableList<Event> {

        val previousBlockEvents = mutableListOf<Event>()

        if (isInitialized) {
            narrative!!.previousEventBlock()?.events?.filter {
                it.isImageEvent()
            }.let { previousBlockEvents.addAll(it ?: emptyList()) }

            narrative!!.previousEventBlock()?.events?.filter {
                it.isMusicEvent()
            }.let { previousBlockEvents.addAll(it ?: emptyList()) }
        }

        return previousBlockEvents
    }

    private fun NarrativeComponent.readyCurrentBlockEvents(blockCumulativeTimer : ImmersionTimer) : MutableList<Event> {

        val currentBlockEvents = mutableListOf<Event>()

        if (isInitialized) {
            narrative!!.previousEventBlock()?.events?.filter {
                (it is ITriggerEvent) &&
                (it as ITriggerEvent).blockTrigger() == ITriggerEvent.EventTrigger.ON_EXIT
            }.let { currentBlockEvents.addAll(it ?: emptyList()) }

            narrative!!.currentEventBlock()?.events?.filter {
                (it is ITriggerEvent) &&
                (it as ITriggerEvent).blockTrigger() == ITriggerEvent.EventTrigger.ON_ENTRY
            }.let { currentBlockEvents.addAll(it ?: emptyList()) }

            narrative!!.currentEventBlock()?.events?.filter {
                (it is ITriggerEvent) &&
                (it.timeTrigger() != null) &&
                blockCumulativeTimer.onOrPast(it.timeTrigger())
            }.let { currentBlockEvents.addAll(it ?: emptyList()) }

            narrative!!.currentEventBlock()?.events?.filter {
                (it !is ITriggerEvent)
            }.let { currentBlockEvents.addAll(it ?: emptyList()) }
        }

        return currentBlockEvents
    }
}