package river.exertion.kcop

import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.inject.Context
import ktx.inject.register
import river.exertion.kcop.simulation.KcopSimulator
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.SdcHandler

class Kcop : KtxGame<KtxScreen>() {

    init {
        KcopSkin.screenWidth = initViewportWidth
        KcopSkin.screenHeight = initViewportHeight
    }

    lateinit var twoBatch : PolygonSpriteBatch

    private val context = Context()

    override fun create() {
        Gdx.app.logLevel = LOG_DEBUG

//        val perspectiveCamera = PerspectiveCamera(75f, initViewportWidth, initViewportHeight )
        val orthoCamera = OrthographicCamera().apply { setToOrtho(false, initViewportWidth, initViewportHeight) }
        val viewport = FitViewport(initViewportWidth, initViewportHeight, orthoCamera)

        twoBatch = PolygonSpriteBatch()
        val stage = Stage(viewport, twoBatch)
        SdcHandler.batch = twoBatch

        context.register {
            bindSingleton(orthoCamera)
            bindSingleton(stage)

            addScreen(KcopSimulator( inject(), inject() ) )
        }

        setScreen<KcopSimulator>()
    }

    companion object {
        val initViewportWidth = 1280F
        val initViewportHeight = 720F

        val title = "koboldCave Operating Platform (kcop) v0.12"
    }

}