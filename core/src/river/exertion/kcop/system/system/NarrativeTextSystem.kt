package river.exertion.kcop.system.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import ktx.ashley.allOf
import river.exertion.kcop.narrative.structure.Event
import river.exertion.kcop.narrative.structure.TimelineEvent
import river.exertion.kcop.system.MessageChannel
import river.exertion.kcop.system.component.NarrativeComponent
import river.exertion.kcop.system.immersionTimer.ImmersionTimer
import river.exertion.kcop.system.view.*

class NarrativeTextSystem : IntervalIteratingSystem(allOf(NarrativeComponent::class).get(), 1/10f) {

    override fun processEntity(entity: Entity) {

        val narrativeComponent = NarrativeComponent.getFor(entity)!!

        if ( (narrativeComponent.narrative != null) && (narrativeComponent.isActive) ) {
            val blockInstTime = narrativeComponent.blockImmersionTimers[narrativeComponent.narrativeCurrBlockId()]?.instImmersionTimer?.immersionTime()
            val blockCuml = narrativeComponent.blockImmersionTimers[narrativeComponent.narrativeCurrBlockId()]?.cumlImmersionTimer
            val narrativeCuml = narrativeComponent.narrativeImmersionTimer.cumlImmersionTimer
            var eventText = ""

            //eventBlocks
            narrativeComponent.narrative!!.previousEventBlock()?.events?.filter {
                it.trigger == "onExit"
            }?.forEach { event ->
                if (event.event() == Event.EventType.SET_FLAG) {
                    if ( !narrativeComponent.flags.contains(event.param) ) {
                        NarrativeComponent.getFor(entity)!!.flags.add(event.param)
                        MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.LogEntry, "flag set: ${event.param}") )
                    }
                }
                if (event.event() == Event.EventType.GET_FLAG) {
                    if ( narrativeComponent.flags.contains(event.param) ) {
                        MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.LogEntry, "flag found: ${event.param}, unsetting") )
                        NarrativeComponent.getFor(entity)!!.flags.remove(event.param)
                    }
                }
                if (event.event() == Event.EventType.TEXT) eventText += "\n${event.param}"
                if (event.event() == Event.EventType.LOG) {
                    MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.LogEntry, event.param) )
                    NarrativeComponent.getFor(entity)!!.narrative!!.eventBlocks.firstOrNull { it.narrativeBlockId == narrativeComponent.narrativePrevBlockId() }?.events?.remove(event)
                }
            }

            var showImages = false

            narrativeComponent.narrative!!.currentEventBlock()?.events?.filter {
                it.trigger == "onEntry"
            }?.forEach { event ->
                if (event.event() == Event.EventType.SHOW_IMAGE_LARGE) {
                    if ( narrativeComponent.narrative!!.textures.keys.contains(event.param) ) {
                        showImages = true
                        MessageChannel.DISPLAY_VIEW_BRIDGE.send(null, DisplayViewMessage(DisplayViewMessageType.IMAGE_LARGE, narrativeComponent.narrative!!.textures[event.param]!!.asset))
                    }
                }
                if (event.event() == Event.EventType.SHOW_IMAGE_MEDIUM) {
                    if ( narrativeComponent.narrative!!.textures.keys.contains(event.param) ) {
                        showImages = true
                        MessageChannel.DISPLAY_VIEW_BRIDGE.send(null, DisplayViewMessage(DisplayViewMessageType.IMAGE_MEDIUM, narrativeComponent.narrative!!.textures[event.param]!!.asset))
                    }
                }
                if (event.event() == Event.EventType.SHOW_IMAGE_SMALL) {
                    if ( narrativeComponent.narrative!!.textures.keys.contains(event.param) ) {
                        showImages = true
                        MessageChannel.DISPLAY_VIEW_BRIDGE.send(null, DisplayViewMessage(DisplayViewMessageType.IMAGE_SMALL, narrativeComponent.narrative!!.textures[event.param]!!.asset))
                    }
                }
                if (event.event() == Event.EventType.SET_FLAG) {
                    if ( !narrativeComponent.flags.contains(event.param) ) {
                        NarrativeComponent.getFor(entity)!!.flags.add(event.param)
                        MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.LogEntry, "flag set: ${event.param}") )
                    }
                }
                if (event.event() == Event.EventType.GET_FLAG) {
                    if ( narrativeComponent.flags.contains(event.param) ) {
                        MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.LogEntry, "flag found: ${event.param}, unsetting") )
                        NarrativeComponent.getFor(entity)!!.flags.remove(event.param)
                    }
                }
                if (event.event() == Event.EventType.TEXT) eventText += "\n${event.param}"
                if (event.event() == Event.EventType.LOG) {
                    MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.LogEntry, event.param) )
                    NarrativeComponent.getFor(entity)!!.narrative!!.eventBlocks.firstOrNull { it.narrativeBlockId == narrativeComponent.narrativeCurrBlockId() }?.events?.remove(event)
                }
            }
            /*
                        { "narrativeBlockId" : "demo_block4", "events" : [
                            { "event" : "text", "trigger" : "onEntry", "param" : "entered demo_block4" },
                            { "event" : "log", "trigger" : "onEntry", "param" : "entered demo_block4" },
                            { "event" : "getFlag", "trigger" : "onEntry", "param" : "demo_block2" },
                            { "event" : "setFlag", "trigger" : "onExit", "param" : "demo_block4" }
                            ]},
            */

            //timelineEventBlocks
            narrativeComponent.narrative!!.currentTimelineEventBlock()?.timelineEvents?.filter {
                blockCuml?.onOrPast(it.immersionTime) == true
            }?.sortedBy {
                ImmersionTimer.inSeconds(it.immersionTime)
            }?.forEach { event ->
                if (event.event() == TimelineEvent.TimelineEventType.TEXT) eventText += "\n${event.param}"
                if (event.event() == TimelineEvent.TimelineEventType.LOG) {
                    MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.LogEntry, event.param) )
                    NarrativeComponent.getFor(entity)!!.narrative!!.timelineEventBlocks.firstOrNull { it.narrativeBlockId == narrativeComponent.narrativeCurrBlockId() }?.timelineEvents?.remove(event)
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

            if (!showImages) {
         //       MessageChannel.DISPLAY_VIEW_BRIDGE.send(null, DisplayViewMessage(DisplayViewMessageType.IMAGE_CLEAR, null))
            }


            MessageChannel.TEXT_VIEW_BRIDGE.send(null, TextViewMessage(text, narrativeComponent.narrative!!.currentPrompts(), narrativeComponent.narrative!!.id))
        }

//        SystemManager.logDebug(this.javaClass.name, "${this.interval} passed, timeActive: ${TimeComponent.getFor(entity)!!.timeActive()}, timeRender: ${TimeComponent.getFor(entity)!!.timeRender()}")
//        SystemManager.logDebug(this.javaClass.name, "instImmersionTime: ${immersionTimeComponent.instImmersionTime()}, cumlImmersionTime: ${immersionTimeComponent.cumlImmersionTime()}, localTime:${immersionTimeComponent.localTime()}" )
    }
}