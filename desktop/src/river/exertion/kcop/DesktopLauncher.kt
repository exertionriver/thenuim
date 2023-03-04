package river.exertion.kcop

import com.badlogic.gdx.Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration

object DesktopLauncher {

    val windowWidth = 1280
    val windowHeight = 720

    @JvmStatic
    fun main(arg: Array<String>) {
        val config = Lwjgl3ApplicationConfiguration().apply {
            this.setTitle("koboldCave Operating Platform (kcop) v0.8")
            this.setWindowedMode(windowWidth, windowHeight)
            this.setForegroundFPS(60)
            setBackBufferConfig(8, 8, 8, 8, 24, 0, 8)
        }

        Lwjgl3Application(Kcop(), config).logLevel = Application.LOG_DEBUG
    }
}