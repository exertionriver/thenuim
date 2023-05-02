package river.exertion.kcop

import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ai.msg.TelegramProvider
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.inject.Context
import ktx.inject.register
import river.exertion.kcop.assets.AssetManagerHandlerCl
import river.exertion.kcop.assets.KcopSkin
import river.exertion.kcop.simulation.ProfileSimulator
import river.exertion.kcop.system.ecs.EngineHandler
import river.exertion.kcop.system.messaging.MessageChannelEnum
import river.exertion.kcop.view.SdcHandler

class Kcop : KtxGame<KtxScreen>(), TelegramProvider {

    init {
        MessageChannelEnum.TWO_BATCH_BRIDGE.enableProvider(this)
        MessageChannelEnum.SDC_BRIDGE.enableProvider(this)
        MessageChannelEnum.KCOP_SKIN_BRIDGE.enableProvider(this)
    }

    lateinit var twoBatch : PolygonSpriteBatch
    lateinit var sdcHandler : SdcHandler
    lateinit var assetManagerHandlerCl : AssetManagerHandlerCl
//    val threeBatch = ModelBatch()

    private val context = Context()

    override fun create() {
        Gdx.app.logLevel = loglevel

//        val perspectiveCamera = PerspectiveCamera(75f, initViewportWidth, initViewportHeight )
        val orthoCamera = OrthographicCamera().apply { setToOrtho(false, initViewportWidth, initViewportHeight) }
        val viewport = FitViewport(initViewportWidth, initViewportHeight, orthoCamera)

        twoBatch = PolygonSpriteBatch()
        val stage = Stage(viewport, twoBatch)

        sdcHandler = SdcHandler(twoBatch, KcopSkin.BackgroundColor)
        val engineHandler = EngineHandler()
        assetManagerHandlerCl = AssetManagerHandlerCl()

        context.register {
            bindSingleton(orthoCamera)
            bindSingleton(stage)
            bindSingleton(engineHandler)
            bindSingleton(assetManagerHandlerCl)

            addScreen(ProfileSimulator( inject(), inject(), inject(), inject() ) )
        }

        setScreen<ProfileSimulator>()
    }

    companion object {
        val initViewportWidth = 1280F
        val initViewportHeight = 720F

        val title = "koboldCave Operating Platform (kcop) v0.11"
        val loglevel = LOG_DEBUG
    }

    override fun provideMessageInfo(msg: Int, receiver: Telegraph?): Any {
        if (msg == MessageChannelEnum.TWO_BATCH_BRIDGE.id()) return twoBatch

        if (msg == MessageChannelEnum.SDC_BRIDGE.id()) return sdcHandler
        if (msg == MessageChannelEnum.KCOP_SKIN_BRIDGE.id()) return assetManagerHandlerCl.kcopSkin()
        return false
    }
}