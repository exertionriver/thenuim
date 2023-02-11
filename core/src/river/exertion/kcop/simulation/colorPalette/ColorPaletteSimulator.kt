package river.exertion.kcop.simulation.colorPalette

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g3d.*
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import ktx.app.KtxScreen
import ktx.scene2d.*
import river.exertion.kcop.*
import river.exertion.kcop.assets.FontAssets
import river.exertion.kcop.assets.get
import river.exertion.kcop.assets.load


class ColorPaletteSimulator(private val batch: Batch,
                            private val assets: AssetManager,
                            private val stage: Stage,
                            private val orthoCamera: OrthographicCamera) : KtxScreen {

    val cpLayout = ColorPaletteLayout(orthoCamera.viewportWidth, orthoCamera.viewportHeight)

    @Suppress("NewApi")
    override fun render(delta: Float) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        stage.act(Gdx.graphics.deltaTime)
        stage.draw()

        when {
            Gdx.input.isKeyJustPressed(Input.Keys.R) -> { cpLayout.colorBaseIncrR() }
            Gdx.input.isKeyJustPressed(Input.Keys.G) -> { cpLayout.colorBaseIncrG() }
            Gdx.input.isKeyJustPressed(Input.Keys.B) -> { cpLayout.colorBaseIncrB() }
            Gdx.input.isKeyJustPressed(Input.Keys.E) -> { cpLayout.colorBaseDecrR() }
            Gdx.input.isKeyJustPressed(Input.Keys.F) -> { cpLayout.colorBaseDecrG() }
            Gdx.input.isKeyJustPressed(Input.Keys.V) -> { cpLayout.colorBaseDecrB()}

            Gdx.input.isKeyJustPressed(Input.Keys.UP) -> { cpLayout.colorBaseIncr() }
            Gdx.input.isKeyJustPressed(Input.Keys.DOWN) -> { cpLayout.colorBaseDecr() }
            Gdx.input.isKeyJustPressed(Input.Keys.LEFT) -> { cpLayout.colorSamplePrev() }
            Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) -> { cpLayout.colorSampleNext() }
        }
    }

    override fun hide() {
    }

    override fun show() {
        FontAssets.values().forEach { assets.load(it) }
        assets.finishLoading()

        Gdx.input.inputProcessor = stage

        cpLayout.bitmapFont = assets[FontAssets.OpenSansRegular]
        cpLayout.batch = batch

        stage.addActor(cpLayout.createSampleSwatchesCtrl())
        stage.addActor(cpLayout.createBaseSwatchesCtrl())
        stage.addActor(cpLayout.createCompSwatchesCtrl())
        stage.addActor(cpLayout.createTriadFirstSwatchesCtrl())
        stage.addActor(cpLayout.createTriadSecondSwatchesCtrl())
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