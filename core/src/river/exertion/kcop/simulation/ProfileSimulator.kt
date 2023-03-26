package river.exertion.kcop.simulation

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.ai.msg.TelegramProvider
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g3d.*
import com.badlogic.gdx.scenes.scene2d.Stage
import ktx.app.KtxScreen
import ktx.scene2d.*
import river.exertion.kcop.assets.*
import river.exertion.kcop.simulation.view.ViewLayout
import river.exertion.kcop.system.*
import river.exertion.kcop.system.ecs.EngineHandler
import river.exertion.kcop.system.ecs.component.NarrativeComponent
import river.exertion.kcop.system.ecs.entity.ProfileEntity
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.view.ViewInputProcessor


class ProfileSimulator(private val stage: Stage,
                       private val engineHandler: EngineHandler,
                       private val assetManagerHandler: AssetManagerHandler,
                       private val orthoCamera: OrthographicCamera) : KtxScreen {

    val layout = ViewLayout(orthoCamera.viewportWidth, orthoCamera.viewportHeight)

    @Suppress("NewApi")
    override fun render(delta: Float) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        stage.act(Gdx.graphics.deltaTime)
        stage.draw()

        engineHandler.engine.update(delta)

        when {
            Gdx.input.isKeyJustPressed(Input.Keys.LEFT) -> {
                val prevNarratives = "test"
            }
        }
    }

    override fun hide() {
    }

    override fun show() {
        FreeTypeFontAssets.values().forEach { assetManagerHandler.assets.load(it) }
        TextureAssets.values().forEach { assetManagerHandler.assets.load(it) }
        ProfileAssets.values().forEach { assetManagerHandler.assets.load(it) }
        NarrativeAssets.values().forEach { assetManagerHandler.assets.load(it) }
        assetManagerHandler.assets.finishLoading()

        val inputMultiplexer = InputMultiplexer()
        inputMultiplexer.addProcessor(ViewInputProcessor())
        inputMultiplexer.addProcessor(stage)
        Gdx.input.inputProcessor = inputMultiplexer

        val textFont = assetManagerHandler.assets[FreeTypeFontAssets.NotoSansSymbolsSemiBold]
        val displayFont = assetManagerHandler.assets[FreeTypeFontAssets.Immortal]

        stage.addActor(layout.createDisplayViewCtrl(displayFont))
        stage.addActor(layout.createTextViewCtrl(textFont, assetManagerHandler.assets[TextureAssets.KoboldA]))
        stage.addActor(layout.createLogViewCtrl(textFont, assetManagerHandler.assets[TextureAssets.KoboldA], assetManagerHandler.assets[TextureAssets.KoboldB]))
        stage.addActor(layout.createStatusViewCtrl(textFont, assetManagerHandler.assets[TextureAssets.KoboldA]))
        stage.addActor(layout.createMenuViewCtrl(textFont, assetManagerHandler.assets[TextureAssets.KoboldA], assetManagerHandler.assets[TextureAssets.KoboldB], assetManagerHandler.assets[TextureAssets.KoboldC]))
        stage.addActor(layout.createInputsViewCtrl(textFont, assetManagerHandler.assets[TextureAssets.KoboldA], assetManagerHandler.assets[TextureAssets.KoboldB], assetManagerHandler.assets[TextureAssets.KoboldC]))
        stage.addActor(layout.createAiViewCtrl(textFont, assetManagerHandler.assets[TextureAssets.BlueSphere], assetManagerHandler.assets[TextureAssets.BlueSphere], assetManagerHandler.assets[TextureAssets.BlueSphere]))
        stage.addActor(layout.createPauseViewCtrl(textFont, assetManagerHandler.assets[TextureAssets.KoboldA], assetManagerHandler.assets[TextureAssets.KoboldB], assetManagerHandler.assets[TextureAssets.KoboldC]))

        engineHandler.instantiateEntity(ProfileEntity::class.java, assetManagerHandler.assets[ProfileAssets.ExertionRiverText])
        engineHandler.addComponent(assetManagerHandler.assets[ProfileAssets.ExertionRiverText].profile!!.id, NarrativeComponent::class.java, null,
            NarrativeComponent.NarrativeComponentInit(assetManagerHandler.assets[NarrativeAssets.NarrativeNavigationTest],
                assetManagerHandler.assets[ProfileAssets.ExertionRiverText].profile!!.currentImmersionBlockId!!,
                assetManagerHandler.assets[ProfileAssets.ExertionRiverText].profile!!.statuses.first { it.key == assetManagerHandler.assets[NarrativeAssets.NarrativeNavigationTest].narrative?.id }.cumlImmersionTime!!))

//        layout.currentInstImmersionTimerId = ImmersionTimerComponent.getFor(profileEntity)!!.instImmersionTimer.id
//        layout.currentCumlImmersionTimerId = ImmersionTimerComponent.getFor(profileEntity)!!.cumlImmersionTimer.id
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
        layout.dispose()
    }

}