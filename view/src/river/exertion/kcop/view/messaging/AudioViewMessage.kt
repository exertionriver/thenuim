package river.exertion.kcop.view.messaging

import com.badlogic.gdx.audio.Music
import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler

data class AudioViewMessage(val messageType : AudioViewMessageType, val music : Music?) {

    init {
        MessageChannelHandler.addChannel(MessageChannel(AudioViewBridge, this::class))
    }

    //sound is music played once
    enum class AudioViewMessageType {
        PlaySound, PlayMusic, StopMusic, FadeInMusic, FadeOutMusic, CrossFadeMusic
    }

    companion object {
        const val AudioViewBridge = "AudioViewBridge"
    }
}

