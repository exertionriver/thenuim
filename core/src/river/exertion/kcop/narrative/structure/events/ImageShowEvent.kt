package river.exertion.kcop.narrative.structure.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import river.exertion.kcop.narrative.structure.NarrativeImmersion
import river.exertion.kcop.system.messaging.MessageChannelEnum
import river.exertion.kcop.system.messaging.messages.NarrativeFlagsMessage
import river.exertion.kcop.system.messaging.messages.NarrativeMediaMessage

@Serializable
@SerialName("showImage")
class ImageShowEvent(
    override val type: String = "",
    override val imageFile: String = "",
    override val layoutPaneIdx: String = ""
) : Event(), IImageEvent {

    override fun execEvent(previousEvent : Event?) {
        MessageChannelEnum.NARRATIVE_FLAGS_BRIDGE.send(null, NarrativeFlagsMessage(NarrativeFlagsMessage.NarrativeFlagsMessageType.SetBlockFlag, "exec_${id!!}", NarrativeImmersion.EventFiredValue) )
        MessageChannelEnum.NARRATIVE_MEDIA_BRIDGE.send(null, NarrativeMediaMessage(NarrativeMediaMessage.NarrativeMediaMessageType.ShowImage, imageFile, layoutPaneIdx) )
    }

    override fun resolveEvent(currentEvent: Event?) {
        if (currentEvent == null) {
            MessageChannelEnum.NARRATIVE_FLAGS_BRIDGE.send(null, NarrativeFlagsMessage(NarrativeFlagsMessage.NarrativeFlagsMessageType.SetBlockFlag, "resolve_${id!!}", NarrativeImmersion.EventFiredValue) )
            MessageChannelEnum.NARRATIVE_MEDIA_BRIDGE.send(null, NarrativeMediaMessage(NarrativeMediaMessage.NarrativeMediaMessageType.HideImage, imageFile, layoutPaneIdx) )
        }
    }
}