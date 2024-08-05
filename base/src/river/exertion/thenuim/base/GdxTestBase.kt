package river.exertion.thenuim.base

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Graphics
import com.badlogic.gdx.backends.headless.HeadlessApplication
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.GL30
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class GdxTestBase : ApplicationListener, TestBase() {

    var gdxTestRunning = false
    var gdxTestShowRender = false

    @BeforeAll
    fun init() {
        if (Gdx.app == null) {
            val conf = HeadlessApplicationConfiguration()
            HeadlessApplication(this, conf)
        }
        if (Gdx.gl == null) {
            Gdx.gl = mockk<GL20>(relaxed = true)
            Gdx.gl20 = mockk<GL20>(relaxed = true)
            Gdx.gl30 = mockk<GL30>(relaxed = true)
            Gdx.graphics = mockk<Graphics>(relaxed = true)
        }
    }

    //each test is on the hook for enabling GdxTestRunning, which enables render() loop
    //gdxShowRender can be set per test suite, as desired
    @AfterEach
    fun disableGdxTestRunning() {
        gdxTestRunning = false
    }

    override fun create() {
        TnmBase.create(true)
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun render() {
        if (gdxTestRunning) {
            if (gdxTestShowRender) {
                Log.test("render() delta time", Gdx.graphics.deltaTime.toString())
            }
            TnmBase.render(Gdx.graphics.deltaTime)
        }
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun dispose() {
    }

}