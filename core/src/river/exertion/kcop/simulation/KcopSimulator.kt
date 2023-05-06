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
import river.exertion.kcop.plugin.IDisplayPackage
import river.exertion.kcop.plugin.IPackage
import river.exertion.kcop.profile.ProfilePackage
import river.exertion.kcop.sim.colorPalette.ColorPalettePackage
import river.exertion.kcop.view.KcopInputProcessor
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.ViewPackage.AudioViewBridge
import river.exertion.kcop.view.ViewPackage.KcopBridge
import river.exertion.kcop.view.layout.ViewLayout
import river.exertion.kcop.view.layout.ViewType
import river.exertion.kcop.view.messaging.AudioViewMessage
import river.exertion.kcop.view.messaging.KcopMessage


class KcopSimulator(private val stage: Stage,
                    private val orthoCamera: OrthographicCamera) : Telegraph, KtxScreen {

    val packages = mutableListOf<IPackage>(
        ProfilePackage()
    )
    val displayPackages = mutableListOf<IDisplayPackage>(
        ColorPalettePackage()
    )

    init {
        packages.forEach { it.loadChannels() }
        displayPackages.forEach { it.loadChannels() }

        MessageChannelHandler.enableReceive(KcopBridge, this)
    }



    val viewLayout = ViewLayout(orthoCamera.viewportWidth, orthoCamera.viewportHeight)
 //   val colorPaletteLayout = ColorPaletteLayout(orthoCamera.viewportWidth, orthoCamera.viewportHeight). apply { this.hide() }
 //   val colorPaletteInputProcessor = ColorPaletteInputProcessor()
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

        EngineHandler.engine.update(delta)

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
        inputMultiplexer.addProcessor(KcopInputProcessor())
        inputMultiplexer.addProcessor(stage)
        Gdx.input.inputProcessor = inputMultiplexer

        viewLayout.build(stage)
        displayPackages[0].build(stage.width, stage.height, stage)
        inputMultiplexer.addProcessor(displayPackages[0].inputProcessor())
 //       colorPaletteLayout.build(stage)

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
        viewLayout.dispose()
    }

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                (MessageChannelHandler.isType(KcopBridge, msg.message) ) -> {
                    val kcopMessage: KcopMessage = MessageChannelHandler.receiveMessage(KcopBridge, msg.extraInfo)

                    when (kcopMessage.kcopMessageType) {
                        KcopMessage.KcopMessageType.FullScreen -> {
                            viewLayout.fullScreen(ViewType.DISPLAY_FULLSCREEN.viewPosition(stage.width, stage.height))
                            displayPackages[0].displayFullScreen(ViewType.DISPLAY_FULLSCREEN.viewPosition(stage.width, stage.height))
                            MessageChannelHandler.send(AudioViewBridge, AudioViewMessage(
                                    AudioViewMessage.AudioViewMessageType.PlaySound, viewLayout.kcopSkin.uiSounds[KcopSkin.UiSounds.Swoosh])
                            )
                        }
                        KcopMessage.KcopMessageType.KcopScreen -> {
                            viewLayout.kcopScreen(ViewType.DISPLAY_FULLSCREEN.viewPosition(stage.width, stage.height))
                            displayPackages[0].displayKcopScreen(ViewType.DISPLAY_FULLSCREEN.viewPosition(stage.width, stage.height))
                            MessageChannelHandler.send(AudioViewBridge, AudioViewMessage(
                                    AudioViewMessage.AudioViewMessageType.PlaySound, viewLayout.kcopSkin.uiSounds[KcopSkin.UiSounds.Swoosh])
                            )
                        }
                    }

                    return true
                }
            }
        }
        return false
    }


}