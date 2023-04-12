package river.exertion.kcop.system.messaging.messages

import com.badlogic.gdx.audio.Music

data class DisplayViewAudioMessage(val messageType : DisplayViewAudioMessageType, val music : Music?) {

    //sound is music played once
    enum class DisplayViewAudioMessageType {
        PLAY_SOUND, PLAY_MUSIC, STOP_MUSIC, FADE_MUSIC_OUT, FADE_MUSIC_IN, CROSS_FADE_MUSIC
    }
}

