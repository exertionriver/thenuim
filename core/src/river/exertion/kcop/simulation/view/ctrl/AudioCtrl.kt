package river.exertion.kcop.simulation.view.ctrl

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.utils.Timer
import river.exertion.kcop.simulation.view.displayViewMenus.*
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.*

class AudioCtrl() : Telegraph {

    init {
        MessageChannel.DISPLAY_VIEW_AUDIO_BRIDGE.enableReceive(this)
    }

    var currentMusic : Music? = null
    var currentSound : Music? = null

    fun fadeMusicOut() {
        //https://stackoverflow.com/questions/35744181/libgdx-how-to-get-sfx-to-fade-out
        Timer.schedule(object : Timer.Task() {
            override fun run() {
                if ( (currentMusic?.volume ?: 0f) >= .1) currentMusic?.volume = currentMusic?.volume?.minus(.1f) ?: 0f
                else {
                    currentMusic?.stop()
                    currentMusic = null
                    this.cancel()
                }
            }
        }, 0f, .3f)
    }

    fun fadeMusicIn(music : Music?) {
        if (music != currentMusic) {
            currentMusic?.stop()
            currentMusic = music
            currentMusic?.isLooping = true
            currentMusic?.volume = 0f
            currentMusic?.play()

            Timer.schedule(object : Timer.Task() {
                override fun run() {
                    if ( (currentMusic?.volume ?: 1f) <= .9) currentMusic?.volume = currentMusic?.volume?.plus(.1f) ?: 1f
                    else {
                        this.cancel()
                    }
                }
            }, 0f, .3f)
        }
    }

    fun crossFadeMusic(music : Music?) {
        if (music != currentMusic) {
            //fade current music out
            val prevMusic = currentMusic
            Timer.schedule(object : Timer.Task() {
                override fun run() {
                    if ( (prevMusic?.volume ?: 0f) >= .1) prevMusic?.volume = prevMusic?.volume?.minus(.1f) ?: 0f
                    else {
                        prevMusic?.stop()
                        this.cancel()
                    }
                }
            }, 0f, .3f)
            currentMusic = music
            currentMusic?.isLooping = true
            currentMusic?.volume = 0f
            currentMusic?.play()

            Timer.schedule(object : Timer.Task() {
                override fun run() {
                    if ( (currentMusic?.volume ?: 1f) <= .9) currentMusic?.volume = currentMusic?.volume?.plus(.1f) ?: 1f
                    else {
                        this.cancel()
                    }
                }
            }, 0f, .3f)
        }
    }

    fun playMusic(music : Music?) {
        if (music != currentMusic) {
            currentMusic?.stop()
            currentMusic = music
            currentMusic?.isLooping = true
            currentMusic?.volume = 1f
            currentMusic?.play()
        }
    }

    fun stopMusic() {
        currentMusic?.stop()
        currentMusic = null
    }

    fun playSound(sound : Music?) {
        if (sound != currentSound) {
            currentSound = sound
            currentSound?.isLooping = false
            currentSound?.volume = 1f
            currentSound?.play()
        }
    }

    @Suppress("NewApi")
    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            if (MessageChannel.DISPLAY_VIEW_AUDIO_BRIDGE.isType(msg.message) ) {
                val displayViewAudioMessage: DisplayViewAudioMessage = MessageChannel.DISPLAY_VIEW_AUDIO_BRIDGE.receiveMessage(msg.extraInfo)

                when (displayViewAudioMessage.messageType) {
                    DisplayViewAudioMessage.DisplayViewAudioMessageType.FADE_MUSIC_OUT -> fadeMusicOut()
                    DisplayViewAudioMessage.DisplayViewAudioMessageType.FADE_MUSIC_IN -> fadeMusicIn(displayViewAudioMessage.music)
                    DisplayViewAudioMessage.DisplayViewAudioMessageType.CROSS_FADE_MUSIC -> crossFadeMusic(displayViewAudioMessage.music)
                    DisplayViewAudioMessage.DisplayViewAudioMessageType.PLAY_MUSIC -> playMusic(displayViewAudioMessage.music)
                    DisplayViewAudioMessage.DisplayViewAudioMessageType.PLAY_SOUND -> playSound(displayViewAudioMessage.music)
                    DisplayViewAudioMessage.DisplayViewAudioMessageType.STOP_MUSIC -> stopMusic()
                }
                return true
            }
        }
        return false
    }

    fun dispose() {
        currentMusic?.dispose()
        currentSound?.dispose()
    }
}