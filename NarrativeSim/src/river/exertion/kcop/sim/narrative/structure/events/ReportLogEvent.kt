package river.exertion.kcop.sim.narrative.structure.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.sim.narrative.messaging.NarrativeFlagsMessage
import river.exertion.kcop.sim.narrative.messaging.NarrativeFlagsMessage.Companion.NarrativeFlagsBridge
import river.exertion.kcop.sim.narrative.structure.NarrativeImmersion
import river.exertion.kcop.view.ViewPackage.LogViewBridge
import river.exertion.kcop.view.messaging.LogViewMessage

@Serializable
@SerialName("log")
class ReportLogEvent(
    override val type: String = "",
    override val trigger: String = "",
    val report: String = ""
) : Event(), ITriggerEvent {

    override fun execEvent(previousEvent : Event?) {
        MessageChannelHandler.send(LogViewBridge, LogViewMessage(LogViewMessage.LogViewMessageType.LogEntry, report) )
        MessageChannelHandler.send(NarrativeFlagsBridge, NarrativeFlagsMessage(NarrativeFlagsMessage.NarrativeFlagsMessageType.SetPersistFlag, id!!, NarrativeImmersion.EventFiredValue) )
    }
}
