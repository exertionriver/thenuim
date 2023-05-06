package river.exertion.kcop.view.messaging

import com.badlogic.gdx.audio.Music

data class AudioViewMessage(val messageType : AudioViewMessageType, val music : Music?) {

    //sound is music played once
    enum class AudioViewMessageType {
        PlaySound, PlayMusic, StopMusic, FadeInMusic, FadeOutMusic, CrossFadeMusic
    }

    companion object {
    }
}

