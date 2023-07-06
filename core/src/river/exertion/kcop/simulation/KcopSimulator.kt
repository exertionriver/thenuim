package river.exertion.kcop.simulation

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import ktx.app.KtxScreen
import river.exertion.kcop.base.IKlop
import river.exertion.kcop.base.KcopBase
import river.exertion.kcop.ecs.EngineHandler
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.sim.colorPalette.ColorPaletteDisplayKlop
import river.exertion.kcop.sim.narrative.NarrativeKlop
import river.exertion.kcop.view.KcopInputProcessor
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.ViewKlop.KcopBridge
import river.exertion.kcop.view.layout.AudioView
import river.exertion.kcop.view.layout.ViewLayout
import river.exertion.kcop.view.layout.ViewType
import river.exertion.kcop.view.messaging.KcopSimulationMessage


class KcopSimulator : Telegraph, KtxScreen {

    private val packages = mutableListOf<IKlop>(
        NarrativeKlop,
        ColorPaletteDisplayKlop
    )

    init {
        packages.forEach {
            it.load()
        }

        MessageChannelHandler.enableReceive(KcopBridge, this)
    }

    val viewLayout = ViewLayout
    private lateinit var inputMultiplexer : InputMultiplexer

    override fun render(delta: Float) {

        KcopBase.render(delta, EngineHandler.engine)
    }

    override fun hide() {
    }

    override fun show() {
        inputMultiplexer = InputMultiplexer()
        inputMultiplexer.addProcessor(KcopInputProcessor)
        inputMultiplexer.addProcessor(KcopBase.stage)

        NarrativeKlop.inputMultiplexer = inputMultiplexer
        ColorPaletteDisplayKlop.inputMultiplexer = inputMultiplexer

        Gdx.input.inputProcessor = inputMultiplexer

        NarrativeKlop.showView()

        viewLayout.build(KcopBase.stage)
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
        KcopBase.orthoCamera.viewportWidth = width.toFloat()
        KcopBase.orthoCamera.viewportHeight = height.toFloat()
        KcopBase.stage.viewport.update(width, height)
    }

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                (MessageChannelHandler.isType(KcopBridge, msg.message) ) -> {
                    val kcopSimulationMessage: KcopSimulationMessage = MessageChannelHandler.receiveMessage(KcopBridge, msg.extraInfo)

                    when (kcopSimulationMessage.kcopMessageType) {
                        KcopSimulationMessage.KcopMessageType.FullScreen -> {
                            viewLayout.fullScreen(ViewType.DISPLAY_FULLSCREEN.viewPosition(KcopBase.stage.width, KcopBase.stage.height))
                            AudioView.playSound(KcopSkin.uiSounds[KcopSkin.UiSounds.Swoosh])
                        }
                        KcopSimulationMessage.KcopMessageType.KcopScreen -> {
                            viewLayout.kcopScreen(ViewType.DISPLAY_FULLSCREEN.viewPosition(KcopBase.stage.width, KcopBase.stage.height))
                            AudioView.playSound(KcopSkin.uiSounds[KcopSkin.UiSounds.Swoosh])
                        }
                        KcopSimulationMessage.KcopMessageType.ColorPaletteOn -> {
                            NarrativeKlop.hideView()
                            ColorPaletteDisplayKlop.showView()
                        }
                        KcopSimulationMessage.KcopMessageType.ColorPaletteOff -> {
                            ColorPaletteDisplayKlop.hideView()
                            NarrativeKlop.showView()
                        }
                    }

                    return true
                }
            }
        }
        return false
    }

    override fun dispose() {
        packages.forEach {
            it.dispose()
        }

        super.dispose()
    }
}