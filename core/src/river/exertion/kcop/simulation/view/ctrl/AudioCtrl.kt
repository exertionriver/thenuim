package river.exertion.kcop.simulation.view.ctrl

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.utils.Timer
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.*

class AudioCtrl : Telegraph {

    init {
        MessageChannel.DISPLAY_VIEW_AUDIO_BRIDGE.enableReceive(this)
    }

    var currentMusic : Music? = null
    var musicLock : Timer.Task? = null

    var currentSound : Music? = null

    fun crossfadeMusic(prevMusic : Music?, newMusic : Music?) {
        if (prevMusic != newMusic) {
            newMusic?.isLooping = true
            newMusic?.volume = 0f
            newMusic?.play()

            var fadedOut = false
            var fadedIn = false
            this@AudioCtrl.currentMusic = newMusic

            Timer.schedule(object : Timer.Task() {
                override fun run() {
//                    println("musicLock:${this@AudioCtrl.musicLock} current:${currentMusic?.volume}, prev:${prevMusic?.volume}, new:${newMusic?.volume}")
                    if (this@AudioCtrl.musicLock != null && this@AudioCtrl.musicLock == this) {
                        if (prevMusic != null) {
                            if (prevMusic.volume >= .1) {
                                prevMusic.volume -= .1f
                            } else {
                                fadedOut = true
                                prevMusic.stop()
                            }
                        } else {
                            fadedOut = true
                        }

                        if (newMusic != null) {
                            if (newMusic.volume <= .9) {
                                newMusic.volume += .1f
                            } else {
                                fadedIn = true
                            }
                        } else {
                            fadedIn = true
                        }

                        if (fadedIn && fadedOut) {
                            musicLock = null
                            this.cancel()
                        }
                    } else {
                        if (prevMusic != currentMusic) prevMusic?.stop()
                        newMusic?.volume = .1f
                        this.cancel()
                    }
                }
            }.apply { this@AudioCtrl.musicLock = this }, 0f, .3f)
        }
    }


    fun playMusic(music : Music?) {
        if (music != currentMusic) {
            musicLock = null

            currentMusic?.stop()
            currentMusic = music
            currentMusic?.isLooping = true
            currentMusic?.volume = 1f
            currentMusic?.play()
        }
    }

    fun stopMusic() {
        musicLock = null

        currentMusic?.stop()
        currentMusic = null
    }

    fun playSound(sound : Music?) {
        currentSound = sound
        currentSound?.isLooping = false
        currentSound?.volume = 1f
        currentSound?.play()
    }

    @Suppress("NewApi")
    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            if (MessageChannel.DISPLAY_VIEW_AUDIO_BRIDGE.isType(msg.message) ) {
                val displayViewAudioMessage: DisplayViewAudioMessage = MessageChannel.DISPLAY_VIEW_AUDIO_BRIDGE.receiveMessage(msg.extraInfo)

                when (displayViewAudioMessage.messageType) {
                    DisplayViewAudioMessage.DisplayViewAudioMessageType.FadeOutMusic -> crossfadeMusic(currentMusic, null)
                    DisplayViewAudioMessage.DisplayViewAudioMessageType.FadeInMusic-> crossfadeMusic(null, displayViewAudioMessage.music)
                    DisplayViewAudioMessage.DisplayViewAudioMessageType.CrossFadeMusic -> crossfadeMusic(currentMusic, displayViewAudioMessage.music)
                    DisplayViewAudioMessage.DisplayViewAudioMessageType.PlayMusic -> playMusic(displayViewAudioMessage.music)
                    DisplayViewAudioMessage.DisplayViewAudioMessageType.PlaySound -> playSound(displayViewAudioMessage.music)
                    DisplayViewAudioMessage.DisplayViewAudioMessageType.StopMusic -> stopMusic()
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