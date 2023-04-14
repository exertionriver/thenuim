package river.exertion.kcop.system.messaging.messages

import com.badlogic.gdx.audio.Music

data class DisplayViewAudioMessage(val messageType : DisplayViewAudioMessageType, val music : Music?) {

    //sound is music played once
    enum class DisplayViewAudioMessageType {
        PlaySound, PlayMusic, StopMusic, FadeInMusic, FadeOutMusic, CrossFadeMusic
    }
}

