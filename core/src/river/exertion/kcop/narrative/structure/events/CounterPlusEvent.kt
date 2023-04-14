package river.exertion.kcop.narrative.structure.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import river.exertion.kcop.narrative.structure.NarrativeImmersion
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.NarrativeFlagsMessage

@Serializable
@SerialName("plusCounter")
class CounterPlusEvent(
    override val type: String = "",
    override val trigger: String = "",
    val counterKey: String = ""
) : Event(), ITriggerEvent {

    override fun execEvent(previousEvent : Event?) {
        MessageChannel.NARRATIVE_FLAGS_BRIDGE.send(null, NarrativeFlagsMessage(NarrativeFlagsMessage.NarrativeFlagsMessageType.SetFlag, id!!, NarrativeImmersion.EventFiredValue) )
        MessageChannel.NARRATIVE_FLAGS_BRIDGE.send(null, NarrativeFlagsMessage(NarrativeFlagsMessage.NarrativeFlagsMessageType.AddToCounter, counterKey, (1).toString() ) )
    }
}
