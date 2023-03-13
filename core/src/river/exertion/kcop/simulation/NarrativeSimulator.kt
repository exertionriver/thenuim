package river.exertion.kcop.simulation

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g3d.*
import com.badlogic.gdx.scenes.scene2d.Stage
import ktx.app.KtxScreen
import ktx.scene2d.*
import river.exertion.kcop.*
import river.exertion.kcop.assets.*
import river.exertion.kcop.simulation.view.ViewLayout
import river.exertion.kcop.system.*
import river.exertion.kcop.system.ecs.EngineHandler
import river.exertion.kcop.system.ecs.component.NarrativeComponent
import river.exertion.kcop.system.ecs.entity.NarrativeEntity
import river.exertion.kcop.system.view.ViewInputProcessor


class NarrativeSimulator(private val batch: Batch,
                         private val assets: AssetManager,
                         private val stage: Stage,
                         private val orthoCamera: OrthographicCamera) : KtxScreen {

    val viewLayout = ViewLayout(orthoCamera.viewportWidth, orthoCamera.viewportHeight)
    val engineHandler = EngineHandler()

    var narrativesIdx = 0
    lateinit var narratives : List<Entity>

    @Suppress("NewApi")
    override fun render(delta: Float) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        stage.act(Gdx.graphics.deltaTime)
        stage.draw()

        engineHandler.engine.update(delta)

        when {
            Gdx.input.isKeyJustPressed(Input.Keys.LEFT) -> {
                val prevNarrativesIdx = narrativesIdx
                narrativesIdx = (narrativesIdx - 1).coerceAtLeast(0)
                if (prevNarrativesIdx != narrativesIdx) {
                    NarrativeComponent.getFor(narratives[prevNarrativesIdx])!!.inactivate()
                    NarrativeComponent.getFor(narratives[narrativesIdx])!!.activate()

                    viewLayout.resetNarrative(NarrativeComponent.getFor(narratives[narrativesIdx])!!)
                }
            }
            Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) -> {
                val prevNarrativesIdx = narrativesIdx
                narrativesIdx = (narrativesIdx + 1).coerceAtMost(narratives.size - 1)
                if (prevNarrativesIdx != narrativesIdx) {
                    NarrativeComponent.getFor(narratives[prevNarrativesIdx])!!.inactivate()
                    NarrativeComponent.getFor(narratives[narrativesIdx])!!.activate()

                    viewLayout.resetNarrative(NarrativeComponent.getFor(narratives[narrativesIdx])!!)
                }
            }
        }
    }

    override fun hide() {
    }

    override fun show() {
        FreeTypeFontAssets.values().forEach { assets.load(it) }
        TextureAssets.values().forEach { assets.load(it) }
        NarrativeAssets.values().forEach { assets.load(it) }
        assets.finishLoading()

        val multiplexer = InputMultiplexer()
        multiplexer.addProcessor(ViewInputProcessor())
        multiplexer.addProcessor(stage)
        Gdx.input.inputProcessor = multiplexer

        val textFont = assets[FreeTypeFontAssets.NotoSansSymbolsSemiBold]
        val displayFont = assets[FreeTypeFontAssets.Immortal]

        stage.addActor(viewLayout.createDisplayViewCtrl(batch, displayFont))
        stage.addActor(viewLayout.createTextViewCtrl(batch, textFont, assets[TextureAssets.KoboldA]))
        stage.addActor(viewLayout.createLogViewCtrl(batch, textFont, assets[TextureAssets.KoboldA], assets[TextureAssets.KoboldB]))
        stage.addActor(viewLayout.createStatusViewCtrl(batch, textFont, assets[TextureAssets.KoboldA]))
        stage.addActor(viewLayout.createMenuViewCtrl(batch, textFont, assets[TextureAssets.KoboldA], assets[TextureAssets.KoboldB], assets[TextureAssets.KoboldC]))
        stage.addActor(viewLayout.createInputsViewCtrl(batch, textFont, assets[TextureAssets.KoboldA], assets[TextureAssets.KoboldB], assets[TextureAssets.KoboldC]))
        stage.addActor(viewLayout.createAiViewCtrl(batch, textFont, assets[TextureAssets.BlueSphere], assets[TextureAssets.BlueSphere], assets[TextureAssets.BlueSphere]))
        stage.addActor(viewLayout.createPauseViewCtrl(batch, textFont, assets[TextureAssets.KoboldA], assets[TextureAssets.KoboldB], assets[TextureAssets.KoboldC]))

        engineHandler.instantiateEntity(NarrativeEntity::class.java, assets[NarrativeAssets.NarrativeTest])
        engineHandler.instantiateEntity(NarrativeEntity::class.java, assets[NarrativeAssets.NarrativeNavigationTest])
        engineHandler.instantiateEntity(NarrativeEntity::class.java, assets[NarrativeAssets.NarrativeTimelineTest])
        engineHandler.instantiateEntity(NarrativeEntity::class.java, assets[NarrativeAssets.NarrativeLayoutTest])

        narratives = engineHandler.getAll<NarrativeComponent>()

        NarrativeComponent.getFor(narratives[narrativesIdx])!!.begin()

        viewLayout.resetNarrative(NarrativeComponent.getFor(narratives[narrativesIdx])!!)

        stage.addActor(viewLayout.createTextViewCtrl(batch, textFont, assets[TextureAssets.KoboldA]))
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
        assets.dispose()
        viewLayout.dispose()
    }
}