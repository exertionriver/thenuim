package river.exertion.thenuim

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import river.exertion.thenuim.base.TnmBase

object DesktopLauncher {

    val config = Lwjgl3ApplicationConfiguration().apply {
        this.setTitle(TnmBase.appTitle())
        this.setWindowedMode(TnmBase.initViewportWidth.toInt(), TnmBase.initViewportHeight.toInt())
        this.setForegroundFPS(60)
        setBackBufferConfig(8, 8, 8, 8, 24, 0, 8)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        AppArgHandler.setAppArgs(args)

        Lwjgl3Application(Thenuim(), config)
    }
}