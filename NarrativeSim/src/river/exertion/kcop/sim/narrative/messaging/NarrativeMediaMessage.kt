package river.exertion.kcop.sim.narrative.messaging

import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler

data class NarrativeMediaMessage(val narrativeMediaMessageType : NarrativeMediaMessageType, val assetFilename : String, val imageLayoutPaneIdx: String? = null) {

    init {
        MessageChannelHandler.addChannel(MessageChannel(NarrativeMediaBridge, this::class))
    }

    enum class NarrativeMediaMessageType {
        PlaySound, PlayMusic, StopMusic, FadeInMusic, FadeOutMusic, CrossFadeMusic, ShowImage, HideImage, FadeInImage, FadeOutImage, CrossFadeImage,
    }

    companion object {
        const val NarrativeMediaBridge = "NarrativeMediaBridge"
    }
}
