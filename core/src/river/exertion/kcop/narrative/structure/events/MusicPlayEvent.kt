package river.exertion.kcop.narrative.structure.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import river.exertion.kcop.narrative.structure.NarrativeImmersion
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.NarrativeFlagsMessage
import river.exertion.kcop.system.messaging.messages.NarrativeMediaMessage

@Serializable
@SerialName("playMusic")
class MusicPlayEvent(
    override val type: String = "",
    override val musicFile: String = "",
) : Event(), IMusicEvent {

    override fun execEvent(previousEvent : Event?) {
        MessageChannel.NARRATIVE_MEDIA_BRIDGE.send(null, NarrativeMediaMessage(NarrativeMediaMessage.NarrativeMediaMessageType.PlayMusic, musicFile) )
    }

    override fun resolveEvent(currentEvent: Event?) {
        if (currentEvent == null) {
            MessageChannel.NARRATIVE_MEDIA_BRIDGE.send(null, NarrativeMediaMessage(NarrativeMediaMessage.NarrativeMediaMessageType.StopMusic, musicFile) )
        }
    }
}