package river.exertion.kcop.simulation

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.Stage
import ktx.app.KtxScreen
import river.exertion.kcop.base.IKlop
import river.exertion.kcop.ecs.EngineHandler
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.profile.ProfileKlop
import river.exertion.kcop.sim.colorPalette.ColorPaletteKlopDisplay
import river.exertion.kcop.sim.narrative.NarrativeKlop
import river.exertion.kcop.view.KcopInputProcessor
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.ViewKlop.KcopBridge
import river.exertion.kcop.view.layout.AudioView
import river.exertion.kcop.view.layout.ViewLayout
import river.exertion.kcop.view.layout.ViewType
import river.exertion.kcop.view.messaging.KcopSimulationMessage


class KcopSimulator(private val stage: Stage,
                    private val orthoCamera: OrthographicCamera) : Telegraph, KtxScreen {

    private val packages = mutableListOf<IKlop>(
        ProfileKlop,
        NarrativeKlop,
        ColorPaletteKlopDisplay
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

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        stage.act(Gdx.graphics.deltaTime)
        stage.draw()

        EngineHandler.update(delta)
    }

    override fun hide() {
    }

    override fun show() {
        inputMultiplexer = InputMultiplexer()
        inputMultiplexer.addProcessor(KcopInputProcessor)
        inputMultiplexer.addProcessor(stage)

        NarrativeKlop.inputMultiplexer = inputMultiplexer
        ColorPaletteKlopDisplay.inputMultiplexer = inputMultiplexer

        Gdx.input.inputProcessor = inputMultiplexer

        NarrativeKlop.showView()

        viewLayout.build(stage)
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
        orthoCamera.viewportWidth = width.toFloat()
        orthoCamera.viewportHeight = height.toFloat()
        stage.viewport.update(width, height)
    }

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                (MessageChannelHandler.isType(KcopBridge, msg.message) ) -> {
                    val kcopSimulationMessage: KcopSimulationMessage = MessageChannelHandler.receiveMessage(KcopBridge, msg.extraInfo)

                    when (kcopSimulationMessage.kcopMessageType) {
                        KcopSimulationMessage.KcopMessageType.FullScreen -> {
                            viewLayout.fullScreen(ViewType.DISPLAY_FULLSCREEN.viewPosition(stage.width, stage.height))
                            AudioView.playSound(KcopSkin.uiSounds[KcopSkin.UiSounds.Swoosh])
                        }
                        KcopSimulationMessage.KcopMessageType.KcopScreen -> {
                            viewLayout.kcopScreen(ViewType.DISPLAY_FULLSCREEN.viewPosition(stage.width, stage.height))
                            AudioView.playSound(KcopSkin.uiSounds[KcopSkin.UiSounds.Swoosh])
                        }
                        KcopSimulationMessage.KcopMessageType.ColorPaletteOn -> {
                            NarrativeKlop.hideView()
                            ColorPaletteKlopDisplay.showView()
                        }
                        KcopSimulationMessage.KcopMessageType.ColorPaletteOff -> {
                            ColorPaletteKlopDisplay.hideView()
                            NarrativeKlop.showView()
                        }
                    }

                    return true
                }
            }
        }
        return false
    }
}