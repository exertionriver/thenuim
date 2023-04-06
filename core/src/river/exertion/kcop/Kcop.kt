package river.exertion.kcop

import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ai.msg.TelegramProvider
import com.badlogic.gdx.ai.msg.Telegraph
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
import river.exertion.kcop.assets.*
import river.exertion.kcop.simulation.NarrativeSimulator
import river.exertion.kcop.simulation.ProfileSimulator
import river.exertion.kcop.simulation.ViewSimulator
import river.exertion.kcop.simulation.colorPalette.ColorPaletteSimulator
import river.exertion.kcop.system.ecs.EngineHandler
import river.exertion.kcop.system.messaging.MessageChannel

class Kcop : KtxGame<KtxScreen>(), TelegramProvider {

    init {
        MessageChannel.TWO_BATCH_BRIDGE.enableProvider(this)
        MessageChannel.FONT_BRIDGE.enableProvider(this)
    }

    lateinit var twoBatch : PolygonSpriteBatch
    lateinit var assetManagerHandler : AssetManagerHandler
//    val threeBatch = ModelBatch()

    private val context = Context()

    override fun create() {
        Gdx.app.logLevel = loglevel

//        val perspectiveCamera = PerspectiveCamera(75f, initViewportWidth, initViewportHeight )
        val orthoCamera = OrthographicCamera().apply { setToOrtho(false, initViewportWidth, initViewportHeight) }
        val viewport = FitViewport(initViewportWidth, initViewportHeight, orthoCamera)

        twoBatch = PolygonSpriteBatch()
        val stage = Stage(viewport, twoBatch)

        val engineHandler = EngineHandler()
        assetManagerHandler = AssetManagerHandler()

        context.register {
            bindSingleton(orthoCamera)
            bindSingleton(stage)
            bindSingleton(engineHandler)
            bindSingleton(assetManagerHandler)

//            addScreen(ColorPaletteSimulator( inject(), inject(), inject() ) )
//            addScreen(ViewSimulator( inject(), inject(), inject(), inject() ) )
            addScreen(ProfileSimulator( inject(), inject(), inject(), inject() ) )
//            addScreen(NarrativeSimulator( inject(), inject(), inject(), inject() ) )
        }

        setScreen<ProfileSimulator>()
    }

    companion object {
        val initViewportWidth = 1280F
        val initViewportHeight = 720F

        val title = "koboldCave Operating Platform (kcop) v0.10"
        val loglevel = LOG_DEBUG
    }

    override fun provideMessageInfo(msg: Int, receiver: Telegraph?): Any {
        if (msg == MessageChannel.TWO_BATCH_BRIDGE.id()) return twoBatch
        if (msg == MessageChannel.FONT_BRIDGE.id()) return assetManagerHandler.fontPackage()

        return false
    }
}