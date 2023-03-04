package river.exertion.kcop.simulation

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g3d.*
import com.badlogic.gdx.scenes.scene2d.Stage
import ktx.app.KtxScreen
import ktx.scene2d.*
import river.exertion.kcop.assets.*
import river.exertion.kcop.simulation.view.ViewLayout
import river.exertion.kcop.system.SystemManager
import river.exertion.kcop.system.component.ImmersionTimerComponent
import river.exertion.kcop.system.entity.ProfileEntity
import river.exertion.kcop.system.view.ViewInputProcessor


class ProfileSimulator(private val batch: Batch,
                       private val assets: AssetManager,
                       private val stage: Stage,
                       private val orthoCamera: OrthographicCamera) : KtxScreen {

    val layout = ViewLayout(orthoCamera.viewportWidth, orthoCamera.viewportHeight)
    val engine = PooledEngine().apply { SystemManager.init(this) }

    @Suppress("NewApi")
    override fun render(delta: Float) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        stage.act(Gdx.graphics.deltaTime)
        stage.draw()

        engine.update(delta)
    }

    override fun hide() {
    }

    override fun show() {
        FreeTypeFontAssets.values().forEach { assets.load(it) }
        TextureAssets.values().forEach { assets.load(it) }
        assets.finishLoading()

        val inputMultiplexer = InputMultiplexer()
        inputMultiplexer.addProcessor(ViewInputProcessor())
        inputMultiplexer.addProcessor(stage)
        Gdx.input.inputProcessor = inputMultiplexer

        val textFont = assets[FreeTypeFontAssets.NotoSansSymbolsSemiBold]
        val displayFont = assets[FreeTypeFontAssets.Immortal]

        stage.addActor(layout.createDisplayViewCtrl(batch, displayFont))
        stage.addActor(layout.createTextViewCtrl(batch, textFont, assets[TextureAssets.KoboldA]))
        stage.addActor(layout.createLogViewCtrl(batch, textFont, assets[TextureAssets.KoboldA], assets[TextureAssets.KoboldB]))
        stage.addActor(layout.createStatusViewCtrl(batch, textFont, assets[TextureAssets.KoboldA]))
        stage.addActor(layout.createMenuViewCtrl(batch, textFont, assets[TextureAssets.KoboldA], assets[TextureAssets.KoboldB], assets[TextureAssets.KoboldC]))
        stage.addActor(layout.createInputsViewCtrl(batch, textFont, assets[TextureAssets.KoboldA], assets[TextureAssets.KoboldB], assets[TextureAssets.KoboldC]))
        stage.addActor(layout.createAiViewCtrl(batch, textFont))
        stage.addActor(layout.createPauseViewCtrl(batch, textFont, assets[TextureAssets.KoboldA], assets[TextureAssets.KoboldB], assets[TextureAssets.KoboldC]))

        val profileEntity = ProfileEntity.instantiate(engine, assets[ProfileAssets.ExertionRiverText])

        layout.displayViewCtrl.currentLayoutIdx = 0
        layout.displayViewCtrl.recreate()

        layout.currentInstImmersionTimerId = ImmersionTimerComponent.getFor(profileEntity)!!.instImmersionTimer.id
        layout.currentCumlImmersionTimerId = ImmersionTimerComponent.getFor(profileEntity)!!.cumlImmersionTimer.id
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