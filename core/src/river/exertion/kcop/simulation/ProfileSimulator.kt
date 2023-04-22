package river.exertion.kcop.simulation

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.scenes.scene2d.Stage
import ktx.app.KtxScreen
import river.exertion.kcop.assets.*
import river.exertion.kcop.simulation.colorPalette.ColorPaletteInputProcessor
import river.exertion.kcop.simulation.colorPalette.ColorPaletteLayout
import river.exertion.kcop.simulation.view.ViewLayout
import river.exertion.kcop.simulation.view.ViewType
import river.exertion.kcop.system.ecs.EngineHandler
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.DisplayViewAudioMessage
import river.exertion.kcop.system.messaging.messages.KcopMessage
import river.exertion.kcop.system.view.ViewInputProcessor


class ProfileSimulator(private val stage: Stage,
                       private val engineHandler: EngineHandler,
                       private val assetManagerHandler: AssetManagerHandler,
                       private val orthoCamera: OrthographicCamera) : Telegraph, KtxScreen {

    init {
        MessageChannel.KCOP_BRIDGE.enableReceive(this)
    }

    val viewLayout = ViewLayout(orthoCamera.viewportWidth, orthoCamera.viewportHeight)
    val colorPaletteLayout = ColorPaletteLayout(orthoCamera.viewportWidth, orthoCamera.viewportHeight). apply { this.hide() }
    val colorPaletteInputProcessor = ColorPaletteInputProcessor()
    lateinit var inputMultiplexer : InputMultiplexer

    /*  //for in-sim narrative nav
        lateinit var defaultProfileComponent : ProfileComponent
        var narrativesIdx = 0
        lateinit var narrativesBlock : NarrativeAssets
    */

    @Suppress("NewApi")
    override fun render(delta: Float) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        stage.act(Gdx.graphics.deltaTime)
        stage.draw()

        engineHandler.engine.update(delta)

/*      //for in-sim narrative nav
        when {
            Gdx.input.isKeyJustPressed(Input.Keys.LEFT) -> {
                val prevNarrativesIdx = narrativesIdx
                narrativesIdx = (narrativesIdx - 1).coerceAtLeast(0)
                if (prevNarrativesIdx != narrativesIdx) {
                    NarrativeComponent.getFor(engineHandler.profileEntity)!!.inactivate()
                    NarrativeComponent.ecsInit(defaultProfileComponent.profile!!, narrativesBlock.values[narrativesIdx].narrative!!)
                }
            }
            Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) -> {
                val prevNarrativesIdx = narrativesIdx
                narrativesIdx = (narrativesIdx + 1).coerceAtMost(narrativesBlock.values.size - 1)
                if (prevNarrativesIdx != narrativesIdx) {
                    NarrativeComponent.getFor(engineHandler.profileEntity)!!.inactivate()
                    NarrativeComponent.ecsInit(defaultProfileComponent.profile!!, narrativesBlock.values[narrativesIdx].narrative!!)
                }
            }
        }*/
    }

    override fun hide() {
    }

    override fun show() {
        inputMultiplexer = InputMultiplexer()
        inputMultiplexer.addProcessor(ViewInputProcessor())
        inputMultiplexer.addProcessor(stage)
        Gdx.input.inputProcessor = inputMultiplexer

        viewLayout.build(stage)
        colorPaletteLayout.build(stage)

/*      //for in-sim narrative nav

        defaultProfileComponent = ProfileComponent.getFor(engineHandler.profileEntity)!!
        narrativesBlock = assetManagerHandler.narrativeAssets
        NarrativeComponent.ecsInit(defaultProfileComponent.profile!!, narrativesBlock.values[narrativesIdx].narrative!!)
*/
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

    override fun dispose() {
        assetManagerHandler.dispose()
        viewLayout.dispose()
    }

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                (MessageChannel.KCOP_BRIDGE.isType(msg.message) ) -> {
                    val kcopMessage: KcopMessage = MessageChannel.KCOP_BRIDGE.receiveMessage(msg.extraInfo)

                    when (kcopMessage.kcopMessageType) {
                        KcopMessage.KcopMessageType.FullScreen -> {
                            viewLayout.fullScreen(ViewType.DISPLAY_FULLSCREEN.viewPosition(stage.width, stage.height))
                            colorPaletteLayout.fullScreen(ViewType.DISPLAY_FULLSCREEN.viewPosition(stage.width, stage.height))
                            MessageChannel.DISPLAY_VIEW_AUDIO_BRIDGE.send(null, DisplayViewAudioMessage(
                                    DisplayViewAudioMessage.DisplayViewAudioMessageType.PlaySound, viewLayout.kcopSkin.uiSounds[KcopSkin.UiSounds.Swoosh]))
                        }
                        KcopMessage.KcopMessageType.KcopScreen -> {
                            viewLayout.kcopScreen(ViewType.DISPLAY_FULLSCREEN.viewPosition(stage.width, stage.height))
                            colorPaletteLayout.kcopScreen(ViewType.DISPLAY_FULLSCREEN.viewPosition(stage.width, stage.height))
                            MessageChannel.DISPLAY_VIEW_AUDIO_BRIDGE.send(null, DisplayViewAudioMessage(
                                    DisplayViewAudioMessage.DisplayViewAudioMessageType.PlaySound, viewLayout.kcopSkin.uiSounds[KcopSkin.UiSounds.Swoosh]))
                        }
                        KcopMessage.KcopMessageType.ShowColorPalette -> {
                            colorPaletteLayout.show()
                            inputMultiplexer.addProcessor(colorPaletteInputProcessor)
                        }
                        KcopMessage.KcopMessageType.HideColorPalette -> {
                            colorPaletteLayout.hide()
                            inputMultiplexer.removeProcessor(colorPaletteInputProcessor)
                        }
                    }

                    return true
                }
            }
        }
        return false
    }


}