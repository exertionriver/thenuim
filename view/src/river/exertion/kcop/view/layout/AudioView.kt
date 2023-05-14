package river.exertion.kcop.view.layout

import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.utils.Timer

object AudioView {

    private var currentMusic : Music? = null
    private var musicLock : Timer.Task? = null

    private var currentSound : Music? = null

    fun crossfadeMusic(prevMusic : Music?, newMusic : Music?) {
        if (prevMusic != newMusic) {
            newMusic?.isLooping = true
            newMusic?.volume = 0f
            newMusic?.play()

            var fadedOut = false
            var fadedIn = false
            this@AudioView.currentMusic = newMusic

            Timer.schedule(object : Timer.Task() {
                override fun run() {
//                    println("musicLock:${this@AudioCtrl.musicLock} current:${currentMusic?.volume}, prev:${prevMusic?.volume}, new:${newMusic?.volume}")
                    if (this@AudioView.musicLock != null && this@AudioView.musicLock == this) {
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
            }.apply { this@AudioView.musicLock = this }, 0f, .3f)
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

    fun fadeOutMusic() {
        crossfadeMusic(currentMusic, null)
    }

    fun fadeInMusic(newMusic : Music) {
        crossfadeMusic(null, newMusic)
    }

    fun crossFadeMusic(newMusic : Music) {
        crossfadeMusic(currentMusic, newMusic)
    }

    fun dispose() {
        currentMusic?.dispose()
        currentSound?.dispose()
    }
}