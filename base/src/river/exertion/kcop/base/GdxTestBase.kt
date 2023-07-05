package river.exertion.kcop.base

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Graphics
import com.badlogic.gdx.backends.headless.HeadlessApplication
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.GL30
import io.mockk.mockk
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class GdxTestBase : ApplicationListener, TestBase() {

    fun init(testClass : ApplicationListener) {
        if (Gdx.app == null) {
            val conf = HeadlessApplicationConfiguration()
            HeadlessApplication(testClass, conf)
        }
        if (Gdx.gl == null) {
            Gdx.gl = mockk<GL20>(relaxed = true)
            Gdx.gl20 = mockk<GL20>(relaxed = true)
            Gdx.gl30 = mockk<GL30>(relaxed = true)
            Gdx.graphics = mockk<Graphics>(relaxed = true)
        }
    }

    override fun create() {
        KcopBase.create(true)
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun render() {
        KcopBase.render(Gdx.app.graphics.deltaTime)
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun dispose() {
    }

}