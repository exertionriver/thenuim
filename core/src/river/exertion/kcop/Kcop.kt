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
import river.exertion.kcop.asset.AssetManagerHandler
import river.exertion.kcop.ecs.ECSPackage
import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.plugin.IPackage
import river.exertion.kcop.simulation.KcopSimulator
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.SdcHandler
import river.exertion.kcop.view.ViewPackage
import river.exertion.kcop.view.ViewPackage.KcopSkinBridge
import river.exertion.kcop.view.ViewPackage.SDCBridge

class Kcop : KtxGame<KtxScreen>(), TelegramProvider {

    val packages = mutableListOf<IPackage>(
        ViewPackage, ECSPackage
    )

    init {
        MessageChannelHandler.addChannel(MessageChannel(TwoBatchBridge, PolygonSpriteBatch::class))
        packages.forEach { it.loadChannels() }

        MessageChannelHandler.enableProvider(TwoBatchBridge, this)
        MessageChannelHandler.enableProvider(SDCBridge, this)
        MessageChannelHandler.enableProvider(KcopSkinBridge, this)
    }

    lateinit var twoBatch : PolygonSpriteBatch
    lateinit var sdcHandler : SdcHandler

    private val context = Context()

    override fun create() {
        Gdx.app.logLevel = loglevel

//        val perspectiveCamera = PerspectiveCamera(75f, initViewportWidth, initViewportHeight )
        val orthoCamera = OrthographicCamera().apply { setToOrtho(false, initViewportWidth, initViewportHeight) }
        val viewport = FitViewport(initViewportWidth, initViewportHeight, orthoCamera)

        twoBatch = PolygonSpriteBatch()
        val stage = Stage(viewport, twoBatch)

        sdcHandler = SdcHandler(twoBatch, KcopSkin.BackgroundColor)

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

        val title = "koboldCave Operating Platform (kcop) v0.11"
        val loglevel = LOG_DEBUG

        const val TwoBatchBridge = "TwoBatchBridge"
    }

    override fun provideMessageInfo(msg: Int, receiver: Telegraph?): Any {
        return when {
            (MessageChannelHandler.isType(TwoBatchBridge, msg)) -> twoBatch
            (MessageChannelHandler.isType(SDCBridge, msg)) -> sdcHandler
            (MessageChannelHandler.isType(KcopSkinBridge, msg)) -> AssetManagerHandler.kcopSkin()
            else -> false
        }
    }
}