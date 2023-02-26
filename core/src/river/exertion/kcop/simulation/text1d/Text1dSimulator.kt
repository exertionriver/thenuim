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
                val prevNarrativesIdx = narrativesIdx
                narrativesIdx = (narrativesIdx - 1).coerceAtLeast(0)
                if (prevNarrativesIdx != narrativesIdx) {
                    NarrativeComponent.getFor(narratives[prevNarrativesIdx])!!.inactivate()
                    NarrativeComponent.getFor(narratives[narrativesIdx])!!.activate()

                    layout.resetNarrative(NarrativeComponent.getFor(narratives[narrativesIdx])!!)
                }
            }
            Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) -> {
                val prevNarrativesIdx = narrativesIdx
                narrativesIdx = (narrativesIdx + 1).coerceAtMost(narratives.size - 1)
                if (prevNarrativesIdx != narrativesIdx) {
                    NarrativeComponent.getFor(narratives[prevNarrativesIdx])!!.inactivate()
                    NarrativeComponent.getFor(narratives[narrativesIdx])!!.activate()

                    layout.resetNarrative(NarrativeComponent.getFor(narratives[narrativesIdx])!!)
                }
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

        val multiplexer = InputMultiplexer()
        multiplexer.addProcessor(ViewInputProcessor())
        multiplexer.addProcessor(stage)
        Gdx.input.inputProcessor = multiplexer

        val font = assets[FreeTypeFontAssets.NotoSansSymbolsSemiBold]

        stage.addActor(layout.createLogViewCtrl(batch, font, assets[TextureAssets.KoboldA], assets[TextureAssets.KoboldB]))
        stage.addActor(layout.createPauseViewCtrl(batch, font, assets[TextureAssets.KoboldA], assets[TextureAssets.KoboldB], assets[TextureAssets.KoboldC]))
        stage.addActor(layout.createInputsViewCtrl(batch, font, assets[TextureAssets.KoboldA], assets[TextureAssets.KoboldB], assets[TextureAssets.KoboldC]))
        stage.addActor(layout.createDisplayViewCtrl(batch, font) )
        stage.addActor(layout.createStatusViewCtrl(batch, font, assets[TextureAssets.KoboldA]) )

        layout.displayViewCtrl.displayViewLayouts[0].layoutMode = false
        layout.displayViewCtrl.recreate()

        narratives = mutableListOf(
            NarrativeEntity.instantiate(engine, assets[NarrativeAssets.NarrativeTest]),
            NarrativeEntity.instantiate(engine, assets[NarrativeAssets.NarrativeNavigationTest]),
            NarrativeEntity.instantiate(engine, assets[NarrativeAssets.NarrativeTimelineTest])
        )

        NarrativeComponent.getFor(narratives[narrativesIdx])!!.begin()

        layout.resetNarrative(NarrativeComponent.getFor(narratives[narrativesIdx])!!)

        stage.addActor(layout.createTextViewCtrl(batch, font, assets[TextureAssets.KoboldA]))
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
        layout.dispose()
    }
}