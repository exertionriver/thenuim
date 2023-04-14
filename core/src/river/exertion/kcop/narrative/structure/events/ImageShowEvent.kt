package river.exertion.kcop.narrative.structure.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.NarrativeMediaMessage

@Serializable
@SerialName("showImage")
class ImageShowEvent(
    override val type: String = "",
    override val imageFile: String = "",
    override val layoutPaneIdx: String = ""
) : Event(), IImageEvent {

    override fun execEvent(previousEvent : Event?) {
        MessageChannel.NARRATIVE_MEDIA_BRIDGE.send(null, NarrativeMediaMessage(NarrativeMediaMessage.NarrativeMediaMessageType.ShowImage, imageFile, layoutPaneIdx) )
    }

    override fun resolveEvent(currentEvent: Event?) {
        if (currentEvent == null) {
            MessageChannel.NARRATIVE_MEDIA_BRIDGE.send(null, NarrativeMediaMessage(NarrativeMediaMessage.NarrativeMediaMessageType.HideImage, imageFile, layoutPaneIdx) )
        }
    }
}