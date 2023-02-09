package river.exertion.kcop

import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.inject.Context
import ktx.inject.register
import river.exertion.kcop.assets.NarrativeNavigationLoader
import river.exertion.kcop.assets.NarrativeSequenceLoader
import river.exertion.kcop.narrative.navigation.NarrativeNavigation
import river.exertion.kcop.narrative.sequence.NarrativeSequence
import river.exertion.kcop.simulation.colorPalette.ColorPaletteSimulator
import river.exertion.kcop.simulation.view.ViewSimulator
import river.exertion.kcop.simulation.text1d.Text1dSimulator

class Kcop : KtxGame<KtxScreen>() {

    private val context = Context()

    override fun create() {
        val perspectiveCamera = PerspectiveCamera(75f, initViewportWidth, initViewportHeight )
        val orthoCamera = OrthographicCamera().apply { setToOrtho(false, initViewportWidth, initViewportHeight) }
        val menuViewport = FitViewport(initViewportWidth, initViewportHeight, orthoCamera)
        val gameBatch = ModelBatch()
        val menuBatch = PolygonSpriteBatch()
        val menuStage = Stage(menuViewport, menuBatch)
        val assets = AssetManager()

        assets.setLoader(NarrativeSequence::class.java, NarrativeSequenceLoader(InternalFileHandleResolver()))
        assets.setLoader(NarrativeNavigation::class.java, NarrativeNavigationLoader(InternalFileHandleResolver()))

        context.register {
            bindSingleton(perspectiveCamera)
            bindSingleton(orthoCamera)
            bindSingleton<Batch>(menuBatch)
            bindSingleton(gameBatch)
            bindSingleton(menuStage)
            bindSingleton(assets)

            addScreen(ColorPaletteSimulator( inject(), inject(), inject(), inject() ) )
            addScreen(ViewSimulator( inject(), inject(), inject(), inject() ) )
            addScreen(Text1dSimulator( inject(), inject(), inject(), inject() ) )
        }
//        Gdx.app.logLevel = LOG_DEBUG

        setScreen<ViewSimulator>()

    }

    companion object {
        val initViewportWidth = 1280F
        val initViewportHeight = 720F
    }
}