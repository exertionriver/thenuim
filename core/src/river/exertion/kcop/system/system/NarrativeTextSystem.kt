package river.exertion.kcop.system.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import ktx.ashley.allOf
import river.exertion.kcop.system.MessageChannel
import river.exertion.kcop.system.component.NarrativeComponent
import river.exertion.kcop.system.immersionTimer.ImmersionTimer
import river.exertion.kcop.system.view.LogViewMessage
import river.exertion.kcop.system.view.LogViewMessageType
import river.exertion.kcop.system.view.TextViewMessage

class NarrativeTextSystem : IntervalIteratingSystem(allOf(NarrativeComponent::class).get(), 1/10f) {

    override fun processEntity(entity: Entity) {

        val narrativeComponent = NarrativeComponent.getFor(entity)!!

        if (narrativeComponent.narrative != null) {
            val blockInstTime = narrativeComponent.blockImmersionTimers[narrativeComponent.narrativeBlockId()]?.instImmersionTimer?.immersionTime()
            val blockCuml = narrativeComponent.blockImmersionTimers[narrativeComponent.narrativeBlockId()]?.cumlImmersionTimer
            val narrativeCuml = narrativeComponent.narrativeImmersionTimer.cumlImmersionTimer
            var eventText = ""

            //eventBlockEvents
            narrativeComponent.narrative!!.currentEventBlock()?.events?.filter {
                blockCuml?.onOrPast(it.immersionTime) == true
            }?.sortedBy {
                ImmersionTimer.inSeconds(it.immersionTime)
            }?.forEach { event ->
                if (event.event == "text") eventText += "\n${event.param}"
                if (event.event == "log") {
                    MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.LogEntry, event.param) )
                    NarrativeComponent.getFor(entity)!!.narrative!!.eventBlocks.firstOrNull { it.narrativeBlockId == narrativeComponent.narrativeBlockId() }?.events?.remove(event)
                }
            }

            //timelineEvents
            narrativeComponent.narrative!!.timelineEvents.filter {
                narrativeCuml.onOrPast(it.immersionTime)
            }.sortedBy {
                ImmersionTimer.inSeconds(it.immersionTime)
            }.forEach { event ->
                if (event.event == "text") eventText += "\n${event.param}"
                if (event.event == "log") {
                    MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.LogEntry, event.param) )
                    NarrativeComponent.getFor(entity)!!.narrative!!.timelineEvents.remove(event)
                }
            }

            val text = "${narrativeComponent.narrative!!.currentText()}${eventText}\nblock inst time:[$blockInstTime]\nblock cuml time:[${blockCuml?.immersionTime()}]"



            MessageChannel.TEXT_VIEW_BRIDGE.send(null, TextViewMessage(text, narrativeComponent.narrative!!.currentPrompts(), narrativeComponent.narrative!!.id))
        }

//        SystemManager.logDebug(this.javaClass.name, "${this.interval} passed, timeActive: ${TimeComponent.getFor(entity)!!.timeActive()}, timeRender: ${TimeComponent.getFor(entity)!!.timeRender()}")
//        SystemManager.logDebug(this.javaClass.name, "instImmersionTime: ${immersionTimeComponent.instImmersionTime()}, cumlImmersionTime: ${immersionTimeComponent.cumlImmersionTime()}, localTime:${immersionTimeComponent.localTime()}" )
    }
}