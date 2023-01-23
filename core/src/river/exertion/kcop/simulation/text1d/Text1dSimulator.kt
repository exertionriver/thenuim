package river.exertion.kcop.simulation.text1d

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g3d.*
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import ktx.app.KtxScreen
import ktx.graphics.use
import ktx.scene2d.*
import river.exertion.kcop.*
import river.exertion.kcop.assets.*
import river.exertion.kcop.narrative.navigation.NarrativeNavigation
import river.exertion.kcop.simulation.colorPalette.ColorPaletteLayout
import river.exertion.kcop.system.colorPalette.ColorPalette

class Text1dSimulator(private val batch: Batch,
                      private val assets: AssetManager,
                      private val stage: Stage,
                      private val orthoCamera: OrthographicCamera) : KtxScreen {

    val t1Layout = Text1dLayout(orthoCamera.viewportWidth, orthoCamera.viewportHeight)

    @Suppress("NewApi")
    override fun render(delta: Float) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        stage.act(Gdx.graphics.deltaTime)
        stage.draw()

        when {
            Gdx.input.isKeyJustPressed(Input.Keys.LEFT) -> { t1Layout.text1dType.next() }
            Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) -> { t1Layout.text1dType.next() }
            Gdx.input.isKeyJustPressed(Input.Keys.DOWN) -> { t1Layout.text1dType.next() }
            Gdx.input.isKeyJustPressed(Input.Keys.UP) -> { t1Layout.text1dType.next() }
        }
    }

    override fun hide() {
    }

    override fun show() {
        FontAssets.values().forEach { assets.load(it) }
        NarrativeSequenceAssets.values().forEach { assets.load(it) }
        NarrativeNavigationAssets.values().forEach { assets.load(it) }

        assets.finishLoading()

        Gdx.input.inputProcessor = stage

        t1Layout.bitmapFont = assets[FontAssets.OpenSansRegular]
        t1Layout.batch = batch
        t1Layout.text1dSequence = assets[NarrativeSequenceAssets.KCopTest]
        t1Layout.text1dNavigation = assets[NarrativeNavigationAssets.KCopTest]

        stage.addActor(t1Layout.createTextBlockCtrl())

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