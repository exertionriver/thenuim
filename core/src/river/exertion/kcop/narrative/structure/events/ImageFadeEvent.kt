package river.exertion.kcop.narrative.structure.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.NarrativeMediaMessage

@Serializable
@SerialName("fadeImage")
class ImageFadeEvent(
    override val type: String = "",
    override val imageFile: String = "",
    override val layoutPaneIdx: String = ""
) : Event(), IImageEvent {

    override fun execEvent(previousEvent : Event?) {
        if (previousEvent == null) {
            MessageChannel.NARRATIVE_MEDIA_BRIDGE.send(null, NarrativeMediaMessage(NarrativeMediaMessage.NarrativeMediaMessageType.FadeInImage, imageFile, layoutPaneIdx) )
        } else if ( (previousEvent as IImageEvent).imageFile != imageFile ) {
            MessageChannel.NARRATIVE_MEDIA_BRIDGE.send(null, NarrativeMediaMessage(NarrativeMediaMessage.NarrativeMediaMessageType.CrossFadeImage, imageFile, layoutPaneIdx) )
        }
    }

    override fun resolveEvent(currentEvent: Event?) {
        if (currentEvent == null) {
            MessageChannel.NARRATIVE_MEDIA_BRIDGE.send(null, NarrativeMediaMessage(NarrativeMediaMessage.NarrativeMediaMessageType.FadeOutImage, imageFile, layoutPaneIdx) )
        }
    }
}