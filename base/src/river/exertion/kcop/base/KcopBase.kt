package river.exertion.kcop.base

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
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

    val initViewportWidth = 1280F
    val initViewportHeight = 720F

    private val appTitle = "koboldCave Operating Platform (kcop)"
    private val appVersion = "v0.12"

    fun appTitleFull() = "$appTitle $appVersion"

    fun init(isTesting : Boolean? = false) {

        Gdx.app.logLevel = Application.LOG_DEBUG

        orthoCamera = OrthographicCamera().apply { setToOrtho(false, initViewportWidth, initViewportHeight) }

        viewport = if (isTesting == true) mockk<FitViewport>(relaxed = true) else FitViewport(initViewportWidth, initViewportHeight, orthoCamera)

        twoBatch = if (isTesting == true) mockk<PolygonSpriteBatch>(relaxed = true) else PolygonSpriteBatch()

        stage = Stage(viewport, twoBatch)
    }
}