package river.exertion.kcop.base

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import io.mockk.mockk

object KcopBase {

    lateinit var viewport : Viewport
    lateinit var twoBatch : Batch
    lateinit var orthoCamera : OrthographicCamera
    lateinit var stage : Stage
    lateinit var inputMultiplexer : InputMultiplexer

    val initViewportWidth = 1280F
    val initViewportHeight = 720F

    var appName = "koboldCave Operating Platform (kcop)"
    var appVersion = "v0.12"

    fun appTitle() = "$appName $appVersion"

    fun create(isTesting : Boolean? = false) {

        Gdx.app.logLevel = Application.LOG_DEBUG

        orthoCamera = if (isTesting == true) mockk<OrthographicCamera>(relaxed = true) else OrthographicCamera().apply { setToOrtho(false, initViewportWidth, initViewportHeight) }

        viewport = if (isTesting == true) mockk<FitViewport>(relaxed = true) else FitViewport(initViewportWidth, initViewportHeight, orthoCamera)

        twoBatch = if (isTesting == true) mockk<PolygonSpriteBatch>(relaxed = true) else PolygonSpriteBatch()

        stage = if (isTesting == true) mockk<Stage>(relaxed = true) else Stage(viewport, twoBatch)

        inputMultiplexer = InputMultiplexer()

        if (isTesting == false) {
            Gdx.input.inputProcessor = inputMultiplexer
        }
    }

    fun render(delta : Float, pooledEngine : PooledEngine? = null) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        stage.act(delta)
        stage.draw()

        pooledEngine?.update(delta)
    }
}