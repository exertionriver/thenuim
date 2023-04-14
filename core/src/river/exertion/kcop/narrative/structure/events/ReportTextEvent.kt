package river.exertion.kcop.narrative.structure.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import river.exertion.kcop.narrative.structure.NarrativeImmersion
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.NarrativeFlagsMessage

@Serializable
@SerialName("text")
class ReportTextEvent(
    override val type: String = "",
    override val trigger: String = "",
    val report: String = ""
) : Event(), ITriggerEvent {

    override fun execEvent(previousEvent : Event?) {
        MessageChannel.NARRATIVE_FLAGS_BRIDGE.send(null, NarrativeFlagsMessage(NarrativeFlagsMessage.NarrativeFlagsMessageType.SetFlag, id!!, NarrativeImmersion.EventFiredValue) )
    }
}
