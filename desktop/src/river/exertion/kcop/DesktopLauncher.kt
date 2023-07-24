package river.exertion.kcop

import GdxDesktopTestBehavior
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import river.exertion.kcop.base.KcopBase
import river.exertion.kcop.view.layout.ButtonView

object DesktopLauncher {

    private fun config() = Lwjgl3ApplicationConfiguration().apply {
        this.setTitle(KcopBase.appTitleFull())
        this.setWindowedMode(KcopBase.initViewportWidth.toInt(), KcopBase.initViewportHeight.toInt())
        this.setForegroundFPS(60)
        setBackBufferConfig(8, 8, 8, 8, 24, 0, 8)
    }

    @JvmStatic
    fun main(arg: Array<String>) {
        Lwjgl3Application(Kcop(), config())
    }
}