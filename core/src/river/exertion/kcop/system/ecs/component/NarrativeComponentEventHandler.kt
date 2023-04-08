package river.exertion.kcop.system.ecs.component

import river.exertion.kcop.narrative.structure.Event
import river.exertion.kcop.narrative.structure.TimelineEvent
import river.exertion.kcop.system.immersionTimer.ImmersionTimer
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.*

object NarrativeComponentEventHandler {

    fun NarrativeComponent.currentText() : String {
        return if (isInitialized) narrative!!.currentText() else ""
    }

    //outputs aggregate text
    fun NarrativeComponent.executeReadyTimelineEvents() : String {

        var returnText = ""

        if (isInitialized) {

            readyTimelineEvents(timerPair.cumlImmersionTimer, blockImmersionTimers[narrativeCurrBlockId()]!!.cumlImmersionTimer).forEach { timelineEvent ->

                when (timelineEvent.eventType()) {
                    Event.EventType.LOG -> {
                        MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.LogEntry, timelineEvent.param) )

                        narrative!!.timelineEventBlocks.firstOrNull { it.narrativeBlockId == narrativeCurrBlockId() }?.timelineEvents?.remove(timelineEvent)
                        narrative!!.timelineEvents.remove(timelineEvent)
                    }
                    Event.EventType.TEXT -> { returnText += "\n${timelineEvent.param}" }
                    else -> {}
                }
            }
        }

        return returnText
    }

    private fun NarrativeComponent.readyTimelineEvents(narrativeCumulativeTimer : ImmersionTimer, blockCumulativeTimer : ImmersionTimer) : List<TimelineEvent> {

        if (isInitialized) {
            val narrativeTimelineEvents = narrative!!.timelineEvents.filter {
                narrativeCumulativeTimer.onOrPast(it.immersionTime)
            }.sortedBy {
                ImmersionTimer.inSeconds(it.immersionTime)
            }

            if (narrative!!.currentTimelineEventBlock() != null) {
                return narrativeTimelineEvents.plus(
                    narrative!!.currentTimelineEventBlock()!!.readyTimelineBlockEvents(blockCumulativeTimer)
                )
            }

            return narrativeTimelineEvents
        }

        return emptyList()
    }

    @Suppress("NewApi")
    fun NarrativeComponent.executeReadyBlockEvents() : String {

        var returnText = ""
        var counterVal : Int

        if (isInitialized) {

            val previousBlockEvents = readyPreviousBlockEvents()
            val currentBlockEvents = readyCurrentBlockEvents()

            previousBlockEvents.forEach { previousBlockEvent ->

                when (previousBlockEvent.eventType()) {
                    Event.EventType.LOG -> {
                        MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.LogEntry, previousBlockEvent.param) )
                        narrative!!.eventBlocks.firstOrNull { it.narrativeBlockId == narrativePrevBlockId() }?.events?.remove(previousBlockEvent)
                    }
                    Event.EventType.TEXT -> { returnText += "\n${previousBlockEvent.param}" }
                    Event.EventType.SET_FLAG -> {
                        if (!flags.map { it.key }.contains(previousBlockEvent.param)) {
                            flags.add(ImmersionStatus(previousBlockEvent.param))
                        }
                    }
                    Event.EventType.UNSET_FLAG -> {
                        flags.removeIf { it.key == previousBlockEvent.param }
                    }
                    Event.EventType.ZERO_COUNTER -> {
                        if (!flags.map { it.key }.contains("fired_${previousBlockEvent.param}")) {
                            counterVal = 0
                            if (!flags.map { it.key }.contains(previousBlockEvent.param)) {
                                flags.add(ImmersionStatus(previousBlockEvent.param, counterVal.toString()))
                            } else {
                                flags.first { it.key == previousBlockEvent.param }.value = counterVal.toString()
                            }

                            flags.add(ImmersionStatus("fired_${previousBlockEvent.param}"))
                        }
                    }
                    Event.EventType.PLUS_COUNTER -> {
                        if (!flags.map { it.key }.contains("fired_${previousBlockEvent.param}")) {
                            if (!flags.map { it.key }.contains(previousBlockEvent.param)) {
                                counterVal = 1
                                flags.add(ImmersionStatus(previousBlockEvent.param, counterVal.toString()))
                            } else {
                                counterVal = flags.first { it.key == previousBlockEvent.param }.value!!.toInt().plus(1)
                                flags.first { it.key == previousBlockEvent.param }.value = counterVal.toString()
                            }
                            flags.add(ImmersionStatus("fired_${previousBlockEvent.param}"))
                        }
                    }
                    Event.EventType.MINUS_COUNTER -> {
                        if ( !flags.map { it.key }.contains("fired_${previousBlockEvent.param}") ) {
                            if ( !flags.map { it.key }.contains(previousBlockEvent.param) ) {
                                counterVal = -1
                                flags.add(ImmersionStatus(previousBlockEvent.param, counterVal.toString()))
                            } else {
                                counterVal = flags.first { it.key == previousBlockEvent.param }.value!!.toInt().minus(1)
                                flags.first { it.key == previousBlockEvent.param }.value = counterVal.toString()
                            }
                            flags.add(ImmersionStatus("fired_${previousBlockEvent.param}"))
                        }
                    }
                    Event.EventType.SHOW_IMAGE -> {
                        val currentImageEventInSamePane = currentBlockEvents.firstOrNull { Event.EventType.isImageEvent(it.eventType) && (it.param2 == previousBlockEvent.param2) } != null

                        if ( !currentImageEventInSamePane ) {
                            MessageChannel.DISPLAY_VIEW_TEXTURE_BRIDGE.send(null, DisplayViewTextureMessage(
                                DisplayViewTextureMessageType.SHOW_IMAGE, previousBlockEvent.param2.toInt(), null)
                            )
                        }
                    }
                    Event.EventType.FADE_IMAGE -> {
                        val currentImageEventInSamePane = currentBlockEvents.firstOrNull { Event.EventType.isImageEvent(it.eventType) && (it.param2 == previousBlockEvent.param2) } != null

                        if ( !currentImageEventInSamePane ) {
                            MessageChannel.DISPLAY_VIEW_TEXTURE_BRIDGE.send(null, DisplayViewTextureMessage(
                                DisplayViewTextureMessageType.FADE_IMAGE_OUT, previousBlockEvent.param2.toInt(), null)
                            )
                        }
                    }
                    Event.EventType.PLAY_SOUND -> {
                        MessageChannel.DISPLAY_VIEW_AUDIO_BRIDGE.send(null, DisplayViewAudioMessage(
                            DisplayViewAudioMessageType.PLAY_SOUND, null)
                        )
                    }
                    Event.EventType.PLAY_MUSIC -> {
                        val currentMusicPlaying = currentBlockEvents.firstOrNull { Event.EventType.isMusicEvent(it.eventType) } != null

                        if ( !currentMusicPlaying ) {
                            MessageChannel.DISPLAY_VIEW_AUDIO_BRIDGE.send(null, DisplayViewAudioMessage(
                                DisplayViewAudioMessageType.STOP_MUSIC, null)
                            )
                        }
                    }
                    Event.EventType.FADE_MUSIC -> {
                        val currentMusicPlaying = currentBlockEvents.firstOrNull { Event.EventType.isMusicEvent(it.eventType) } != null

                        if ( !currentMusicPlaying ) {
                            MessageChannel.DISPLAY_VIEW_AUDIO_BRIDGE.send(null, DisplayViewAudioMessage(
                                DisplayViewAudioMessageType.FADE_MUSIC_OUT, null)
                            )
                        }
                    }
                    else -> {}
                }
            }

            currentBlockEvents.forEach { currentBlockEvent ->
                when (currentBlockEvent.eventType()) {
                    Event.EventType.LOG -> {
                        MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.LogEntry, currentBlockEvent.param) )
                        narrative!!.eventBlocks.firstOrNull { it.narrativeBlockId == narrativeCurrBlockId() }?.events?.remove(currentBlockEvent)
                    }
                    Event.EventType.TEXT -> { returnText += "\n${currentBlockEvent.param}" }
                    Event.EventType.SET_FLAG -> {
                        if ( !flags.map { it.key }.contains(currentBlockEvent.param) ) {
                            flags.add(ImmersionStatus(currentBlockEvent.param))
//                            MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.LogEntry, "flag set: ${currentBlockEvent.param}") )
                        }
                    }
                    Event.EventType.UNSET_FLAG -> {
                        if ( flags.map { it.key }.contains(currentBlockEvent.param) ) {
//                            MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.LogEntry, "flag found: ${currentBlockEvent.param}, unsetting") )
                            flags.removeIf { it.key == currentBlockEvent.param }
                        }
                    }
                    Event.EventType.ZERO_COUNTER -> {
                        if ( !flags.map { it.key }.contains("fired_${currentBlockEvent.param}") ) {
                            counterVal = 0
                            if ( !flags.map { it.key }.contains(currentBlockEvent.param) ) {
                                flags.add(ImmersionStatus(currentBlockEvent.param, counterVal.toString()))
                            } else {
                                flags.first { it.key == currentBlockEvent.param }.value = counterVal.toString()
                            }

                            flags.add(ImmersionStatus("fired_${currentBlockEvent.param}"))
                        }
                    }
                    Event.EventType.PLUS_COUNTER -> {
                        if ( !flags.map { it.key }.contains("fired_${currentBlockEvent.param}") ) {
                            if ( !flags.map { it.key }.contains(currentBlockEvent.param) ) {
                                counterVal = 1
                                flags.add(ImmersionStatus(currentBlockEvent.param, counterVal.toString()))
                            } else {
                                counterVal = flags.first { it.key == currentBlockEvent.param }.value!!.toInt().plus(1)
                                flags.first { it.key == currentBlockEvent.param }.value = counterVal.toString()
                            }
                            flags.add(ImmersionStatus("fired_${currentBlockEvent.param}"))
                        }
                    }
                    Event.EventType.MINUS_COUNTER -> {
                        if ( !flags.map { it.key }.contains("fired_${currentBlockEvent.param}") ) {
                            if ( !flags.map { it.key }.contains(currentBlockEvent.param) ) {
                                counterVal = -1
                                flags.add(ImmersionStatus(currentBlockEvent.param, counterVal.toString()))
                            } else {
                                counterVal = flags.first { it.key == currentBlockEvent.param }.value!!.toInt().minus(1)
                                flags.first { it.key == currentBlockEvent.param }.value = counterVal.toString()
                            }
                            flags.add(ImmersionStatus("fired_${currentBlockEvent.param}"))
                        }
                    }
                    Event.EventType.SHOW_IMAGE -> {
                        val previousImageEventInSamePane = previousBlockEvents.firstOrNull { Event.EventType.isImageEvent(it.eventType) && (it.param2 == currentBlockEvent.param2) } != null

                        if ( narrative!!.textures.keys.contains(currentBlockEvent.param) ) {
                            MessageChannel.DISPLAY_VIEW_TEXTURE_BRIDGE.send(null, DisplayViewTextureMessage(
                                DisplayViewTextureMessageType.SHOW_IMAGE, currentBlockEvent.param2.toInt(), narrative!!.textures[currentBlockEvent.param]!!.asset)
                            )
                        } else if (previousImageEventInSamePane) {
                            MessageChannel.DISPLAY_VIEW_TEXTURE_BRIDGE.send(null, DisplayViewTextureMessage(
                                DisplayViewTextureMessageType.SHOW_IMAGE, currentBlockEvent.param2.toInt(), null)
                            )
                        }
                    }
                    Event.EventType.FADE_IMAGE -> {
                        val previousImageEventInSamePane = previousBlockEvents.firstOrNull { Event.EventType.isImageEvent(it.eventType) && (it.param2 == currentBlockEvent.param2) } != null

                        if ( narrative!!.textures.keys.contains(currentBlockEvent.param) ) {
                            MessageChannel.DISPLAY_VIEW_TEXTURE_BRIDGE.send(null, DisplayViewTextureMessage(
                                DisplayViewTextureMessageType.CROSSFADE_IMAGE, currentBlockEvent.param2.toInt(), narrative!!.textures[currentBlockEvent.param]!!.asset)
                            )
                        } else if (previousImageEventInSamePane) {
                            MessageChannel.DISPLAY_VIEW_TEXTURE_BRIDGE.send(null, DisplayViewTextureMessage(
                                DisplayViewTextureMessageType.FADE_IMAGE_OUT, currentBlockEvent.param2.toInt(), null)
                            )
                        }
                    }
                    Event.EventType.PLAY_SOUND -> {
                        if ( narrative!!.sounds.keys.contains(currentBlockEvent.param) ) {
                            MessageChannel.DISPLAY_VIEW_AUDIO_BRIDGE.send(null, DisplayViewAudioMessage(
                                DisplayViewAudioMessageType.PLAY_SOUND, narrative!!.sounds[currentBlockEvent.param]!!.asset)
                            )
                        }
                    }
                    Event.EventType.PLAY_MUSIC -> {
                        val previousMusicPlaying = previousBlockEvents.firstOrNull { Event.EventType.isMusicEvent(it.eventType) } != null

                        if ( narrative!!.music.keys.contains(currentBlockEvent.param) ) {
                                MessageChannel.DISPLAY_VIEW_AUDIO_BRIDGE.send(null, DisplayViewAudioMessage(
                                    DisplayViewAudioMessageType.PLAY_MUSIC, narrative!!.music[currentBlockEvent.param]!!.asset)
                                )
                        } else if ( previousMusicPlaying ) {
                            MessageChannel.DISPLAY_VIEW_AUDIO_BRIDGE.send(null, DisplayViewAudioMessage(
                                DisplayViewAudioMessageType.STOP_MUSIC, null)
                            )
                        }
                    }
                    Event.EventType.FADE_MUSIC -> {
                        val previousMusicPlaying = previousBlockEvents.firstOrNull { Event.EventType.isMusicEvent(it.eventType) } != null

                        if ( narrative!!.music.keys.contains(currentBlockEvent.param) ) {
                            if ( previousMusicPlaying ) {
                                MessageChannel.DISPLAY_VIEW_AUDIO_BRIDGE.send(null, DisplayViewAudioMessage(
                                    DisplayViewAudioMessageType.CROSS_FADE_MUSIC, narrative!!.music[currentBlockEvent.param]!!.asset)
                                )
                            } else {
                                MessageChannel.DISPLAY_VIEW_AUDIO_BRIDGE.send(null, DisplayViewAudioMessage(
                                    DisplayViewAudioMessageType.FADE_MUSIC_IN, narrative!!.music[currentBlockEvent.param]!!.asset)
                                )
                            }
                        } else if ( previousMusicPlaying ) {
                            MessageChannel.DISPLAY_VIEW_AUDIO_BRIDGE.send(null, DisplayViewAudioMessage(
                                DisplayViewAudioMessageType.FADE_MUSIC_OUT, null)
                            )
                        }
                    }
                    else -> {}
                }
            }
        }

        return returnText
    }

    fun NarrativeComponent.currentBlockTimer() : String {
        return if (isInitialized) "\nblock inst time:[${blockImmersionTimers[narrativeCurrBlockId()]?.instImmersionTimer?.immersionTime()}]\nblock cuml time:[${blockImmersionTimers[narrativeCurrBlockId()]?.cumlImmersionTimer?.immersionTime()}]"
        else ""
    }

    private fun NarrativeComponent.readyPreviousBlockEvents() : MutableList<Event> {

        val previousBlockEvents = mutableListOf<Event>()

        if (isInitialized) {
            narrative!!.previousEventBlock()?.events?.filter {
                it.eventTrigger() == Event.EventTrigger.ON_EXIT
            }.let { previousBlockEvents.addAll(it ?: emptyList()) }

            narrative!!.previousEventBlock()?.events?.filter {
                Event.EventType.isImageEvent(it.eventType)
            }.let { previousBlockEvents.addAll(it ?: emptyList()) }

            narrative!!.previousEventBlock()?.events?.filter {
                Event.EventType.isMusicEvent(it.eventType)
            }.let { previousBlockEvents.addAll(it ?: emptyList()) }
        }

        return previousBlockEvents
    }

    private fun NarrativeComponent.readyCurrentBlockEvents() : MutableList<Event> {

        val currentBlockEvents = mutableListOf<Event>()

        if (isInitialized) {
            narrative!!.currentEventBlock()?.events?.filter {
                it.eventTrigger() == Event.EventTrigger.ON_ENTRY
            }.let { currentBlockEvents.addAll(it ?: emptyList()) }
        }

        return currentBlockEvents
    }
}