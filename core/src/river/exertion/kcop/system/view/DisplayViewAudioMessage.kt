package river.exertion.kcop.system.view

import com.badlogic.gdx.audio.Music

data class DisplayViewAudioMessage(val messageType : DisplayViewAudioMessageType, val music : Music?)

//sound is music played once
enum class DisplayViewAudioMessageType {
    PLAY_SOUND, PLAY_MUSIC, STOP_MUSIC
}