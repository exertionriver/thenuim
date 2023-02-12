package river.exertion.kcop

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.inject.Context
import ktx.inject.register
import river.exertion.kcop.assets.NarrativeAssetLoader
import river.exertion.kcop.narrative.structure.Narrative
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

        val ifhr = InternalFileHandleResolver()
        assets.setLoader(Narrative::class.java, NarrativeAssetLoader(ifhr))
        assets.setLoader(FreeTypeFontGenerator::class.java, FreeTypeFontGeneratorLoader(ifhr))
        assets.setLoader(BitmapFont::class.java, ".ttf", FreetypeFontLoader(ifhr))

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