package river.exertion.kcop.system.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import ktx.ashley.allOf
import river.exertion.kcop.narrative.structure.Event
import river.exertion.kcop.narrative.structure.TimelineEvent
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.ecs.component.NarrativeComponent
import river.exertion.kcop.system.immersionTimer.ImmersionTimer
import river.exertion.kcop.system.messaging.messages.*

class NarrativeTextSystem : IntervalIteratingSystem(allOf(NarrativeComponent::class).get(), 1/10f) {

    //TO DO: split into two systems, one for changed, one for timeline
    override fun processEntity(entity: Entity) {

        val narrativeComponent = NarrativeComponent.getFor(entity)!!

        if ( (narrativeComponent.narrative != null) && narrativeComponent.isActive) {

            var timelineChanged = false

            val blockInstTime = narrativeComponent.blockImmersionTimers[narrativeComponent.narrativeCurrBlockId()]?.instImmersionTimer?.immersionTime()
            val blockCuml = narrativeComponent.blockImmersionTimers[narrativeComponent.narrativeCurrBlockId()]?.cumlImmersionTimer
            val narrativeCuml = narrativeComponent.narrativeImmersionTimer.cumlImmersionTimer
            var eventText = ""

            //timelineEventBlocks
            narrativeComponent.narrative!!.currentTimelineEventBlock()?.timelineEvents?.filter {
                blockCuml?.onOrPast(it.immersionTime) == true
            }?.sortedBy {
                ImmersionTimer.inSeconds(it.immersionTime)
            }?.forEach { event ->
                if (event.event() == TimelineEvent.TimelineEventType.TEXT) {
                    eventText += "\n${event.param}"
                    timelineChanged = true
                }
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
                if (event.event == "text") {
                    eventText += "\n${event.param}"
                    timelineChanged = true
                }
                if (event.event == "log") {
                    MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.LogEntry, event.param) )
                    NarrativeComponent.getFor(entity)!!.narrative!!.timelineEvents.remove(event)
                }
            }

            if (narrativeComponent.changed || timelineChanged) {

                //eventBlocks
                narrativeComponent.narrative!!.previousEventBlock()?.events?.filter {
                    it.trigger == "onExit"
                }?.forEach { previousEvent ->
                    if (previousEvent.event() == Event.EventType.SET_FLAG) {
                        if ( !narrativeComponent.flags.contains(previousEvent.param) ) {
                            NarrativeComponent.getFor(entity)!!.flags.add(previousEvent.param)
                            MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.LogEntry, "flag set: ${previousEvent.param}") )
                        }
                    }
                    if (previousEvent.event() == Event.EventType.GET_FLAG) {
                        if ( narrativeComponent.flags.contains(previousEvent.param) ) {
                            MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.LogEntry, "flag found: ${previousEvent.param}, unsetting") )
                            NarrativeComponent.getFor(entity)!!.flags.remove(previousEvent.param)
                        }
                    }
                    if (previousEvent.event() == Event.EventType.TEXT) eventText += "\n${previousEvent.param}"
                    if (previousEvent.event() == Event.EventType.LOG) {
                        MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.LogEntry, previousEvent.param) )
                        NarrativeComponent.getFor(entity)!!.narrative!!.eventBlocks.firstOrNull { it.narrativeBlockId == narrativeComponent.narrativePrevBlockId() }?.events?.remove(previousEvent)
                    }
                }

                //stop past image display
                narrativeComponent.narrative!!.previousEventBlock()?.events?.filter {
                    Event.EventType.isImageEvent(it.event)
                }?.forEach { previousEvent ->
                    val currentImageEventInSamePane = narrativeComponent.narrative!!.currentEventBlock()?.events?.firstOrNull { Event.EventType.isImageEvent(it.event) && (it.param2 == previousEvent.param2) }

                    if (previousEvent.event() == Event.EventType.SHOW_IMAGE) {
                        if ( currentImageEventInSamePane == null) {
                            MessageChannel.DISPLAY_VIEW_TEXTURE_BRIDGE.send(null, DisplayViewTextureMessage(
                                DisplayViewTextureMessageType.SHOW_IMAGE, previousEvent.param2.toInt(), null)
                            )
                        } else {
                            MessageChannel.DISPLAY_VIEW_TEXTURE_BRIDGE.send(null, DisplayViewTextureMessage(
                                DisplayViewTextureMessageType.CROSSFADE_IMAGE, previousEvent.param2.toInt(), narrativeComponent.narrative!!.textures[currentImageEventInSamePane.param]!!.asset)
                            )
                        }
                    }
                    if (previousEvent.event() == Event.EventType.FADE_IMAGE) {
                        if ( currentImageEventInSamePane == null) {
                            MessageChannel.DISPLAY_VIEW_TEXTURE_BRIDGE.send(null, DisplayViewTextureMessage(
                                DisplayViewTextureMessageType.FADE_IMAGE_OUT, previousEvent.param2.toInt(), null)
                            )
                        } else {
                            MessageChannel.DISPLAY_VIEW_TEXTURE_BRIDGE.send(null, DisplayViewTextureMessage(
                                DisplayViewTextureMessageType.CROSSFADE_IMAGE, previousEvent.param2.toInt(), narrativeComponent.narrative!!.textures[currentImageEventInSamePane.param]!!.asset)
                            )
                        }
                    }
                }

                //stop past audio
                narrativeComponent.narrative!!.previousEventBlock()?.events?.filter {
                    Event.EventType.isAudioEvent(it.event)
                }?.forEach { previousEvent ->
                    if (previousEvent.event() == Event.EventType.FADE_MUSIC) {
                        if ( narrativeComponent.narrative!!.currentEventBlock()?.events?.firstOrNull { Event.EventType.isAudioEvent(it.event) } == null) {
                            MessageChannel.DISPLAY_VIEW_AUDIO_BRIDGE.send(null, DisplayViewAudioMessage(
                                DisplayViewAudioMessageType.FADE_MUSIC_OUT, null)
                            )
                        } else {
                            val newMusic = narrativeComponent.narrative!!.currentEventBlock()?.events?.firstOrNull { Event.EventType.isAudioEvent(it.event) }!!
                            MessageChannel.DISPLAY_VIEW_AUDIO_BRIDGE.send(null, DisplayViewAudioMessage(
                                DisplayViewAudioMessageType.CROSS_FADE_MUSIC, narrativeComponent.narrative!!.music[newMusic.param]!!.asset)
                            )
                        }
                    }
                    if (previousEvent.event() == Event.EventType.PLAY_MUSIC) {
                        if ( narrativeComponent.narrative!!.currentEventBlock()?.events?.firstOrNull { Event.EventType.isAudioEvent(it.event) } == null) {
                            MessageChannel.DISPLAY_VIEW_AUDIO_BRIDGE.send(null, DisplayViewAudioMessage(
                                DisplayViewAudioMessageType.STOP_MUSIC, null)
                            )
                        }
                    }
                    if (previousEvent.event() == Event.EventType.PLAY_SOUND) {
                        if ( narrativeComponent.narrative!!.currentEventBlock()?.events?.firstOrNull { it.event() == Event.EventType.PLAY_SOUND } == null) {
                            MessageChannel.DISPLAY_VIEW_AUDIO_BRIDGE.send(null, DisplayViewAudioMessage(
                                DisplayViewAudioMessageType.PLAY_SOUND, null)
                            )
                        }
                    }
                }


                narrativeComponent.narrative!!.currentEventBlock()?.events?.filter {
                    it.trigger == "onEntry"
                }?.forEach { currentEvent ->
                    if (currentEvent.event() == Event.EventType.PLAY_MUSIC) {
                        if ( narrativeComponent.narrative!!.music.keys.contains(currentEvent.param) ) {
                            MessageChannel.DISPLAY_VIEW_AUDIO_BRIDGE.send(null, DisplayViewAudioMessage(
                                DisplayViewAudioMessageType.PLAY_MUSIC, narrativeComponent.narrative!!.music[currentEvent.param]!!.asset)
                            )
                        }
                    }
                    if (currentEvent.event() == Event.EventType.FADE_MUSIC) {
                        if ( narrativeComponent.narrative!!.music.keys.contains(currentEvent.param) ) {
                            MessageChannel.DISPLAY_VIEW_AUDIO_BRIDGE.send(null, DisplayViewAudioMessage(
                                DisplayViewAudioMessageType.FADE_MUSIC_IN, narrativeComponent.narrative!!.music[currentEvent.param]!!.asset)
                            )
                        }
                    }
                    if (currentEvent.event() == Event.EventType.PLAY_SOUND) {
                        if ( narrativeComponent.narrative!!.sounds.keys.contains(currentEvent.param) ) {
                            MessageChannel.DISPLAY_VIEW_AUDIO_BRIDGE.send(null, DisplayViewAudioMessage(
                                DisplayViewAudioMessageType.PLAY_SOUND, narrativeComponent.narrative!!.sounds[currentEvent.param]!!.asset)
                            )
                        }
                    }
                    if (currentEvent.event() == Event.EventType.FADE_IMAGE) {
                        if ( narrativeComponent.narrative!!.textures.keys.contains(currentEvent.param) ) {
                            MessageChannel.DISPLAY_VIEW_TEXTURE_BRIDGE.send(null, DisplayViewTextureMessage(
                                DisplayViewTextureMessageType.FADE_IMAGE_IN, currentEvent.param2.toInt(), narrativeComponent.narrative!!.textures[currentEvent.param]!!.asset)
                            )
                        }
                    }
                    if (currentEvent.event() == Event.EventType.SHOW_IMAGE) {
                        if ( narrativeComponent.narrative!!.textures.keys.contains(currentEvent.param) ) {
                            MessageChannel.DISPLAY_VIEW_TEXTURE_BRIDGE.send(null, DisplayViewTextureMessage(
                                DisplayViewTextureMessageType.SHOW_IMAGE, currentEvent.param2.toInt(), narrativeComponent.narrative!!.textures[currentEvent.param]!!.asset)
                            )
                        }
                    }
                    if (currentEvent.event() == Event.EventType.SET_FLAG) {
                        if ( !narrativeComponent.flags.contains(currentEvent.param) ) {
                            NarrativeComponent.getFor(entity)!!.flags.add(currentEvent.param)
                            MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.LogEntry, "flag set: ${currentEvent.param}") )
                        }
                    }
                    if (currentEvent.event() == Event.EventType.GET_FLAG) {
                        if ( narrativeComponent.flags.contains(currentEvent.param) ) {
                            MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.LogEntry, "flag found: ${currentEvent.param}, unsetting") )
                            NarrativeComponent.getFor(entity)!!.flags.remove(currentEvent.param)
                        }
                    }
                    if (currentEvent.event() == Event.EventType.TEXT) eventText += "\n${currentEvent.param}"
                    if (currentEvent.event() == Event.EventType.LOG) {
                        MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.LogEntry, currentEvent.param) )
                        NarrativeComponent.getFor(entity)!!.narrative!!.eventBlocks.firstOrNull { it.narrativeBlockId == narrativeComponent.narrativeCurrBlockId() }?.events?.remove(currentEvent)
                    }
                }


                val text = "${narrativeComponent.narrative!!.currentText()}${eventText}\nblock inst time:[$blockInstTime]\nblock cuml time:[${blockCuml?.immersionTime()}]"

                MessageChannel.TEXT_VIEW_BRIDGE.send(null, TextViewMessage(text, narrativeComponent.narrative!!.currentDisplayText(), narrativeComponent.narrative!!.currentFontSize(), narrativeComponent.narrative!!.currentPrompts(), narrativeComponent.narrative!!.id))
                MessageChannel.DISPLAY_VIEW_TEXT_BRIDGE.send(null, TextViewMessage(text, narrativeComponent.narrative!!.currentDisplayText(), narrativeComponent.narrative!!.currentFontSize(), narrativeComponent.narrative!!.currentPrompts(), narrativeComponent.narrative!!.layoutTag))

                NarrativeComponent.getFor(entity)!!.changed = false
            }
        }
//        SystemManager.logDebug(this.javaClass.name, "${this.interval} passed, timeActive: ${TimeComponent.getFor(entity)!!.timeActive()}, timeRender: ${TimeComponent.getFor(entity)!!.timeRender()}")
//        SystemManager.logDebug(this.javaClass.name, "instImmersionTime: ${immersionTimeComponent.instImmersionTime()}, cumlImmersionTime: ${immersionTimeComponent.cumlImmersionTime()}, localTime:${immersionTimeComponent.localTime()}" )
    }
}