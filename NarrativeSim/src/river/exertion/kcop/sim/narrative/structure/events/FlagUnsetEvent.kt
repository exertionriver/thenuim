package river.exertion.kcop.sim.narrative.structure.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.sim.narrative.messaging.NarrativeFlagsMessage
import river.exertion.kcop.sim.narrative.messaging.NarrativeFlagsMessage.Companion.NarrativeFlagsBridge
import river.exertion.kcop.sim.narrative.structure.NarrativeImmersion

@Serializable
@SerialName("unsetFlag")
class FlagUnsetEvent(
    override val type: String = "",
    override val trigger: String = "",
    val flagKey: String = ""
) : Event(), ITriggerEvent {

    override fun execEvent(previousEvent : Event?) {
        MessageChannelHandler.send(NarrativeFlagsBridge, NarrativeFlagsMessage(NarrativeFlagsMessage.NarrativeFlagsMessageType.SetPersistFlag, id!!, NarrativeImmersion.EventFiredValue) )
        MessageChannelHandler.send(NarrativeFlagsBridge, NarrativeFlagsMessage(NarrativeFlagsMessage.NarrativeFlagsMessageType.UnsetPersistFlag, flagKey) )
    }
}
