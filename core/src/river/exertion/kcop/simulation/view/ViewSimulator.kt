package river.exertion.kcop.simulation.view

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
import river.exertion.kcop.assets.FontAssets
import river.exertion.kcop.assets.TextureAssets
import river.exertion.kcop.assets.get
import river.exertion.kcop.assets.load
import river.exertion.kcop.system.SystemManager
import river.exertion.kcop.system.entity.Observer
import river.exertion.kcop.system.view.ViewInputProcessor


class ViewSimulator(private val batch: Batch,
                    private val assets: AssetManager,
                    private val stage: Stage,
                    private val orthoCamera: OrthographicCamera) : KtxScreen {

    val layout = ViewLayout(orthoCamera.viewportWidth, orthoCamera.viewportHeight)

    val engine = PooledEngine().apply { SystemManager.init(this) }
    val observer = Observer.instantiate(engine)

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
        FontAssets.values().forEach { assets.load(it) }
        TextureAssets.values().forEach { assets.load(it) }
        assets.finishLoading()

        val inputMultiplexer = InputMultiplexer()
        inputMultiplexer.addProcessor(ViewInputProcessor())
        inputMultiplexer.addProcessor(stage)
        Gdx.input.inputProcessor = inputMultiplexer

        stage.addActor(layout.createDisplayViewCtrl(batch, assets[FontAssets.OpenSansRegular]))
        stage.addActor(layout.createTextViewCtrl(batch, assets[FontAssets.OpenSansRegular]))
        stage.addActor(layout.createLogViewCtrl(batch, assets[FontAssets.OpenSansRegular], assets[TextureAssets.KoboldA], assets[TextureAssets.KoboldB]))
        stage.addActor(layout.createMenuViewCtrl(batch, assets[FontAssets.OpenSansRegular]))
        stage.addActor(layout.createPromptsViewCtrl(batch, assets[FontAssets.OpenSansRegular]))
        stage.addActor(layout.createInputsViewCtrl(batch, assets[FontAssets.OpenSansRegular], assets[TextureAssets.KoboldA], assets[TextureAssets.KoboldB], assets[TextureAssets.KoboldC]))
        stage.addActor(layout.createAiViewCtrl(batch, assets[FontAssets.OpenSansRegular]))
        stage.addActor(layout.createPauseViewCtrl(batch, assets[FontAssets.OpenSansRegular], assets[TextureAssets.KoboldA], assets[TextureAssets.KoboldB], assets[TextureAssets.KoboldC]))
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