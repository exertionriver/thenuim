package river.exertion.kcop.narrative.structure.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import river.exertion.kcop.narrative.structure.NarrativeImmersion
import river.exertion.kcop.system.messaging.MessageChannelEnum
import river.exertion.kcop.system.messaging.messages.NarrativeFlagsMessage

@Serializable
@SerialName("unsetFlag")
class FlagUnsetEvent(
    override val type: String = "",
    override val trigger: String = "",
    val flagKey: String = ""
) : Event(), ITriggerEvent {

    override fun execEvent(previousEvent : Event?) {
        MessageChannelEnum.NARRATIVE_FLAGS_BRIDGE.send(null, NarrativeFlagsMessage(NarrativeFlagsMessage.NarrativeFlagsMessageType.SetPersistFlag, id!!, NarrativeImmersion.EventFiredValue) )
        MessageChannelEnum.NARRATIVE_FLAGS_BRIDGE.send(null, NarrativeFlagsMessage(NarrativeFlagsMessage.NarrativeFlagsMessageType.UnsetPersistFlag, flagKey) )
    }
}
