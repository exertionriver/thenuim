package river.exertion.kcop.base

import com.badlogic.gdx.Application
import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.headless.HeadlessApplication
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class GdxTestBase : ApplicationListener, TestBase() {

    fun init(testClass : ApplicationListener) {
        if (Gdx.app == null) {
            val conf = HeadlessApplicationConfiguration()
            HeadlessApplication(testClass, conf)
        }
    }

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun render() {
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun dispose() {
    }

}