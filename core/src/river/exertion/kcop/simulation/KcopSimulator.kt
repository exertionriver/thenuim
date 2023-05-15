package river.exertion.kcop.simulation

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.Stage
import ktx.app.KtxScreen
import river.exertion.kcop.ecs.EngineHandler
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.plugin.IPackage
import river.exertion.kcop.profile.ProfilePackage
import river.exertion.kcop.sim.colorPalette.ColorPalettePackage
import river.exertion.kcop.sim.narrative.NarrativePackage
import river.exertion.kcop.view.KcopInputProcessor
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.ViewPackage.KcopBridge
import river.exertion.kcop.view.layout.AudioView
import river.exertion.kcop.view.layout.DisplayView
import river.exertion.kcop.view.layout.ViewLayout
import river.exertion.kcop.view.layout.ViewType
import river.exertion.kcop.view.messaging.KcopSimulationMessage


class KcopSimulator(private val stage: Stage,
                    private val orthoCamera: OrthographicCamera) : Telegraph, KtxScreen {

    val packages = mutableListOf<IPackage>(
        ProfilePackage,
        NarrativePackage,
        ColorPalettePackage
    )

    init {
        packages.forEach {
            it.load()
        }

        MessageChannelHandler.enableReceive(KcopBridge, this)
    }

    val viewLayout = ViewLayout
 //   val colorPaletteLayout = ColorPaletteLayout(orthoCamera.viewportWidth, orthoCamera.viewportHeight). apply { this.hide() }
 //   val colorPaletteInputProcessor = ColorPaletteInputProcessor()
    lateinit var inputMultiplexer : InputMultiplexer

    @Suppress("NewApi")
    override fun render(delta: Float) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        stage.act(Gdx.graphics.deltaTime)
        stage.draw()

        EngineHandler.engine.update(delta)
    }

    override fun hide() {
    }

    override fun show() {
        inputMultiplexer = InputMultiplexer()
        inputMultiplexer.addProcessor(KcopInputProcessor)
        inputMultiplexer.addProcessor(stage)
        Gdx.input.inputProcessor = inputMultiplexer

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
                            ColorPalettePackage.build()
                            inputMultiplexer.addProcessor(ColorPalettePackage.inputProcessor())
                        }
                        KcopSimulationMessage.KcopMessageType.ColorPaletteOff -> {
                            DisplayView.currentDisplayView = null
                            DisplayView.build()
                            inputMultiplexer.removeProcessor(ColorPalettePackage.inputProcessor())
                        }
                    }

                    return true
                }
            }
        }
        return false
    }
}