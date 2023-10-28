package river.exertion.kcop

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import river.exertion.kcop.asset.AppArgHandler
import river.exertion.kcop.base.KcopBase

object DesktopLauncher {

    private fun config() = Lwjgl3ApplicationConfiguration().apply {
        this.setTitle(KcopBase.appTitleFull())
        this.setWindowedMode(KcopBase.initViewportWidth.toInt(), KcopBase.initViewportHeight.toInt())
        this.setForegroundFPS(60)
        setBackBufferConfig(8, 8, 8, 8, 24, 0, 8)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        AppArgHandler.setAppArgs(args)
        Lwjgl3Application(Kcop(), config())
    }
}