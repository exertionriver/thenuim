package river.exertion.kcop.simulation.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g3d.*
import com.badlogic.gdx.scenes.scene2d.Stage
import ktx.app.KtxScreen
import ktx.scene2d.*
import river.exertion.kcop.assets.FontAssets
import river.exertion.kcop.assets.get
import river.exertion.kcop.assets.load

class ViewSimulator(private val batch: Batch,
                    private val assets: AssetManager,
                    private val stage: Stage,
                    private val orthoCamera: OrthographicCamera) : KtxScreen {

    val layout = ViewLayout(orthoCamera.viewportWidth, orthoCamera.viewportHeight)

    @Suppress("NewApi")
    override fun render(delta: Float) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        stage.act(Gdx.graphics.deltaTime)
        stage.draw()
    }

    override fun hide() {
    }

    override fun show() {
        FontAssets.values().forEach { assets.load(it) }
        assets.finishLoading()

        Gdx.input.inputProcessor = stage

        layout.bitmapFont = assets[FontAssets.OpenSansRegular]
        layout.batch = batch

        stage.addActor(layout.createDisplayViewCtrl())
        stage.addActor(layout.createTextViewCtrl())
        stage.addActor(layout.createLogViewCtrl())
        stage.addActor(layout.createMenuViewCtrl())
        stage.addActor(layout.createPromptsViewCtrl())
        stage.addActor(layout.createInputsViewCtrl())
        stage.addActor(layout.createAiViewCtrl())
        stage.addActor(layout.createPauseViewCtrl())
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
        orthoCamera.viewportWidth = width.toFloat()
        orthoCamera.viewportHeight = height.toFloat()
    }

    override fun dispose() {
        assets.dispose()
    }
}