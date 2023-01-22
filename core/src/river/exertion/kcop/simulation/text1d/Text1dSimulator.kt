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
import river.exertion.kcop.assets.FontAssets
import river.exertion.kcop.assets.NarrativeAssets
import river.exertion.kcop.assets.get
import river.exertion.kcop.assets.load
import river.exertion.kcop.system.colorPalette.ColorPalette

class Text1dSimulator(private val batch: Batch,
                      private val assets: AssetManager,
                      private val stage: Stage,
                      private val orthoCamera: OrthographicCamera) : KtxScreen {

    val textColor = ColorPalette.of("skyBlue")

    @Suppress("NewApi")
    override fun render(delta: Float) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        when {
            Gdx.input.isKeyJustPressed(Input.Keys.DOWN) -> { assets[NarrativeAssets.KCopTest].next() }
            Gdx.input.isKeyJustPressed(Input.Keys.UP) -> { assets[NarrativeAssets.KCopTest].prev() }
        }

/*        batch.use {
            assets[FontAssets.OpenSansRegular].drawLabel(batch, Vector2(100f, 400f),
                assets[NarrativeAssets.KCopTest].currentText(), textColor.color())
        }
*/    }

    override fun hide() {
    }

    override fun show() {
        FontAssets.values().forEach { assets.load(it) }
        NarrativeAssets.values().forEach { assets.load(it) }
        assets.finishLoading()
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