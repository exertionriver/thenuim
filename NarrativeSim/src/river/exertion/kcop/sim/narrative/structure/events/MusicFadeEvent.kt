package river.exertion.kcop.sim.narrative.structure.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.sim.narrative.messaging.NarrativeFlagsMessage
import river.exertion.kcop.sim.narrative.messaging.NarrativeFlagsMessage.Companion.NarrativeFlagsBridge
import river.exertion.kcop.sim.narrative.messaging.NarrativeMediaMessage
import river.exertion.kcop.sim.narrative.messaging.NarrativeMediaMessage.Companion.NarrativeMediaBridge
import river.exertion.kcop.sim.narrative.structure.NarrativeImmersion

@Serializable
@SerialName("fadeMusic")
class MusicFadeEvent(
    override val type: String = "",
    override val musicFile: String = "",
) : Event(), IMusicEvent {

    override fun execEvent(previousEvent : Event?) {
        if (previousEvent == null) {
            MessageChannelHandler.send(NarrativeFlagsBridge, NarrativeFlagsMessage(NarrativeFlagsMessage.NarrativeFlagsMessageType.SetBlockFlag, "exec_${id!!}", NarrativeImmersion.EventFiredValue) )
            MessageChannelHandler.send(NarrativeMediaBridge, NarrativeMediaMessage(NarrativeMediaMessage.NarrativeMediaMessageType.FadeInMusic, musicFile) )
        } else if ( (previousEvent as IMusicEvent).musicFile != musicFile ) {
            MessageChannelHandler.send(NarrativeFlagsBridge, NarrativeFlagsMessage(NarrativeFlagsMessage.NarrativeFlagsMessageType.SetBlockFlag, "exec_${id!!}", NarrativeImmersion.EventFiredValue) )
            MessageChannelHandler.send(NarrativeMediaBridge, NarrativeMediaMessage(NarrativeMediaMessage.NarrativeMediaMessageType.CrossFadeMusic, musicFile) )
        }
    }

    override fun resolveEvent(currentEvent: Event?) {
        if (currentEvent == null) {
            MessageChannelHandler.send(NarrativeFlagsBridge, NarrativeFlagsMessage(NarrativeFlagsMessage.NarrativeFlagsMessageType.SetBlockFlag, "resolve_${id!!}", NarrativeImmersion.EventFiredValue) )
            MessageChannelHandler.send(NarrativeMediaBridge, NarrativeMediaMessage(NarrativeMediaMessage.NarrativeMediaMessageType.FadeOutMusic, musicFile) )
        }
    }
}