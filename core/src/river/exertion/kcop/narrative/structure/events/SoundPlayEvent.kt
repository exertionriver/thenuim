package river.exertion.kcop.narrative.structure.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import river.exertion.kcop.narrative.structure.NarrativeImmersion
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.*

@Serializable
@SerialName("playSound")
class SoundPlayEvent(
    override val type: String = "",
    override val trigger: String = "",
    override val musicFile: String = "",
) : Event(), ISoundEvent, ITriggerEvent {

    override fun execEvent(previousEvent : Event?) {
        MessageChannel.NARRATIVE_MEDIA_BRIDGE.send(null, NarrativeMediaMessage(NarrativeMediaMessage.NarrativeMediaMessageType.PlaySound, musicFile) )
        MessageChannel.NARRATIVE_FLAGS_BRIDGE.send(null, NarrativeFlagsMessage(NarrativeFlagsMessage.NarrativeFlagsMessageType.SetFlag, id!!, NarrativeImmersion.EventFiredValue) )
    }
}
