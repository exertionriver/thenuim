package river.exertion.kcop.simulation

import com.badlogic.ashley.core.PooledEngine
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
import river.exertion.kcop.system.SystemManager


class ColorPaletteSimulator(private val menuBatch: Batch,
                            private val assets: AssetManager,
                            private val menuStage: Stage,
                            private val menuCamera: OrthographicCamera) : KtxScreen {

    val engine = PooledEngine().apply { SystemManager.init(this) }

    val cpLayout = ColorPaletteLayout(menuCamera.viewportWidth, menuCamera.viewportHeight)

    val stage = Stage()

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
        cpLayout.batch = menuBatch

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
        menuCamera.viewportWidth = width.toFloat()
        menuCamera.viewportHeight = height.toFloat()
    }

    override fun dispose() {
        assets.dispose()
    }
}