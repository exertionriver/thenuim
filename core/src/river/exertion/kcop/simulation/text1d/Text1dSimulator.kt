package river.exertion.kcop.simulation.text1d

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
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
import river.exertion.kcop.system.SystemManager
import river.exertion.kcop.system.component.ImmersionTimerComponent
import river.exertion.kcop.system.component.NarrativeComponent
import river.exertion.kcop.system.entity.NarrativeEntity
import river.exertion.kcop.system.view.ViewInputProcessor


class Text1dSimulator(private val batch: Batch,
                      private val assets: AssetManager,
                      private val stage: Stage,
                      private val orthoCamera: OrthographicCamera) : KtxScreen {

    val layout = ViewLayout(orthoCamera.viewportWidth, orthoCamera.viewportHeight)

    val engine = PooledEngine().apply { SystemManager.init(this) }

    var narrativesIdx = 0
    lateinit var narratives : MutableList<Entity>

    @Suppress("NewApi")
    override fun render(delta: Float) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        stage.act(Gdx.graphics.deltaTime)
        stage.draw()

        engine.update(delta)

        when {
            Gdx.input.isKeyJustPressed(Input.Keys.LEFT) -> {
                NarrativeComponent.getFor(narratives[narrativesIdx])!!.isActive = false
                narrativesIdx = (narrativesIdx - 1).coerceAtLeast(0)

                NarrativeComponent.getFor(narratives[narrativesIdx])!!.isActive = true

                layout.currentInstImmersionTimerId = ImmersionTimerComponent.getFor(narratives[narrativesIdx])!!.instImmersionTimer.id
                layout.currentCumlImmersionTimerId = ImmersionTimerComponent.getFor(narratives[narrativesIdx])!!.cumlImmersionTimer.id
                layout.currentNarrativeId = NarrativeComponent.getFor(narratives[narrativesIdx])!!.narrative!!.id
                layout.isPaused = ImmersionTimerComponent.getFor(narratives[narrativesIdx])!!.cumlImmersionTimer.isPaused()
            }
            Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) -> {
                NarrativeComponent.getFor(narratives[narrativesIdx])!!.isActive = false
                narrativesIdx = (narrativesIdx + 1).coerceAtMost(narratives.size - 1)

                NarrativeComponent.getFor(narratives[narrativesIdx])!!.isActive = true

                layout.currentInstImmersionTimerId = ImmersionTimerComponent.getFor(narratives[narrativesIdx])!!.instImmersionTimer.id
                layout.currentCumlImmersionTimerId = ImmersionTimerComponent.getFor(narratives[narrativesIdx])!!.cumlImmersionTimer.id
                layout.currentNarrativeId = NarrativeComponent.getFor(narratives[narrativesIdx])!!.narrative!!.id
                layout.isPaused = ImmersionTimerComponent.getFor(narratives[narrativesIdx])!!.cumlImmersionTimer.isPaused()
            }
        }
    }

    override fun hide() {
    }

    override fun show() {
//        BitmapFontAssets.values().forEach { assets.load(it) }
        FreeTypeFontAssets.values().forEach { assets.load(it) }
        TextureAssets.values().forEach { assets.load(it) }
        NarrativeAssets.values().forEach { assets.load(it) }
        assets.finishLoading()

        narratives = mutableListOf(
            NarrativeEntity.instantiate(engine, assets[NarrativeAssets.NarrativeTest]),
            NarrativeEntity.instantiate(engine, assets[NarrativeAssets.NarrativeNavigationTest]),
            NarrativeEntity.instantiate(engine, assets[NarrativeAssets.NarrativeTimelineTest])
        )

        val multiplexer = InputMultiplexer()
        multiplexer.addProcessor(ViewInputProcessor())
        multiplexer.addProcessor(stage)
        Gdx.input.inputProcessor = multiplexer

        val font = assets[FreeTypeFontAssets.NotoSansSymbolsSemiBold]

        NarrativeComponent.getFor(narratives[narrativesIdx])!!.isActive = true

        stage.addActor(layout.createTextViewCtrl(batch, font, NarrativeComponent.getFor(narratives[narrativesIdx])!!.narrative!!.id, assets[TextureAssets.KoboldA]))
        stage.addActor(layout.createLogViewCtrl(batch, font, assets[TextureAssets.KoboldA], assets[TextureAssets.KoboldB]))
        stage.addActor(layout.createPauseViewCtrl(batch, font, assets[TextureAssets.KoboldA], assets[TextureAssets.KoboldB], assets[TextureAssets.KoboldC]))

        layout.currentInstImmersionTimerId = ImmersionTimerComponent.getFor(narratives[narrativesIdx])!!.instImmersionTimer.id
        layout.currentCumlImmersionTimerId = ImmersionTimerComponent.getFor(narratives[narrativesIdx])!!.cumlImmersionTimer.id

        layout.isPaused = ImmersionTimerComponent.getFor(narratives[narrativesIdx])!!.cumlImmersionTimer.isPaused()
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
    }
}