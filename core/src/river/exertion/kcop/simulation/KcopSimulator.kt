package river.exertion.kcop.simulation

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import ktx.app.KtxScreen
import river.exertion.kcop.base.KcopBase
import river.exertion.kcop.ecs.EngineHandler
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.sim.colorPalette.ColorPaletteDisplayKlop
import river.exertion.kcop.sim.narrative.NarrativeKlop
import river.exertion.kcop.view.KcopInputProcessor
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.ViewKlop.KcopBridge
import river.exertion.kcop.view.klop.IDisplayViewKlop
import river.exertion.kcop.view.layout.AudioView
import river.exertion.kcop.view.layout.ViewLayout
import river.exertion.kcop.view.layout.ViewType
import river.exertion.kcop.view.messaging.KcopSimulationMessage


class KcopSimulator : Telegraph, KtxScreen {

    private val displayViewPackages = mutableListOf<IDisplayViewKlop>(
        NarrativeKlop,
        ColorPaletteDisplayKlop
    )

    init {
        displayViewPackages.forEach {
            it.load()
        }

        MessageChannelHandler.enableReceive(KcopBridge, this)
    }

    private lateinit var inputMultiplexer : InputMultiplexer
    var currentDisplayViewKlop : IDisplayViewKlop = NarrativeKlop


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
        ViewLayout.build(KcopBase.stage)
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
                            ViewLayout.fullScreen(ViewType.DISPLAY_FULLSCREEN.viewPosition(KcopBase.stage.width, KcopBase.stage.height))
                            AudioView.playSound(KcopSkin.uiSounds[KcopSkin.UiSounds.Swoosh])
                        }
                        KcopSimulationMessage.KcopMessageType.KcopScreen -> {
                            ViewLayout.kcopScreen(ViewType.DISPLAY_FULLSCREEN.viewPosition(KcopBase.stage.width, KcopBase.stage.height))
                            AudioView.playSound(KcopSkin.uiSounds[KcopSkin.UiSounds.Swoosh])
                        }
                        KcopSimulationMessage.KcopMessageType.ColorPaletteOn -> {
                            currentDisplayViewKlop = ColorPaletteDisplayKlop

                            displayViewPackages.filter { it != currentDisplayViewKlop }.forEach {
                                it.hideView()
                            }

                            currentDisplayViewKlop.showView()
                        }
                        KcopSimulationMessage.KcopMessageType.ColorPaletteOff -> {
                            currentDisplayViewKlop = NarrativeKlop

                            displayViewPackages.filter { it != currentDisplayViewKlop }.forEach {
                                it.hideView()
                            }

                            currentDisplayViewKlop.showView()
                        }
                    }

                    return true
                }
            }
        }
        return false
    }

    override fun dispose() {
        displayViewPackages.forEach {
            it.dispose()
        }

        super.dispose()
    }
}