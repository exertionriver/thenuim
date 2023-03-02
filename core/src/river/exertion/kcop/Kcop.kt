package river.exertion.kcop

import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver
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
import river.exertion.kcop.assets.NarrativeAsset
import river.exertion.kcop.assets.NarrativeAssetLoader
import river.exertion.kcop.assets.ProfileAsset
import river.exertion.kcop.assets.ProfileAssetLoader
import river.exertion.kcop.simulation.NarrativeSimulator
import river.exertion.kcop.simulation.ViewSimulator

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

        val lfhr = LocalFileHandleResolver()
        assets.setLoader(ProfileAsset::class.java, ProfileAssetLoader(lfhr))

        val ifhr = InternalFileHandleResolver()
        assets.setLoader(NarrativeAsset::class.java, NarrativeAssetLoader(ifhr))
        assets.setLoader(FreeTypeFontGenerator::class.java, FreeTypeFontGeneratorLoader(ifhr))
        assets.setLoader(BitmapFont::class.java, ".ttf", FreetypeFontLoader(ifhr))

        context.register {
            bindSingleton(perspectiveCamera)
            bindSingleton(orthoCamera)
            bindSingleton<Batch>(menuBatch)
            bindSingleton(gameBatch)
            bindSingleton(menuStage)
            bindSingleton(assets)

//            addScreen(ColorPaletteSimulator( inject(), inject(), inject(), inject() ) )
//            addScreen(ViewSimulator( inject(), inject(), inject(), inject() ) )
            addScreen(NarrativeSimulator( inject(), inject(), inject(), inject() ) )
        }
        Gdx.app.logLevel = LOG_DEBUG

        setScreen<NarrativeSimulator>()

    }

    companion object {
        val initViewportWidth = 1280F
        val initViewportHeight = 720F
    }
}