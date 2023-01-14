package river.exertion.kcop.simulation

import com.badlogic.ashley.core.PooledEngine
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
import river.exertion.kcop.ColorPalette
import river.exertion.kcop.ShapeDrawerConfig
import river.exertion.kcop.assets.FontAssets
import river.exertion.kcop.assets.get
import river.exertion.kcop.assets.load
import river.exertion.kcop.drawLabel
import river.exertion.kcop.system.SystemManager
import river.exertion.kcop.system.entity.Observer

class ColorPaletteSimulator(private val menuBatch: Batch,
                            private val assets: AssetManager,
                            private val menuStage: Stage,
                            private val menuCamera: OrthographicCamera) : KtxScreen {

    val engine = PooledEngine().apply { SystemManager.init(this) }
    val observer = Observer.instantiate(engine)
    val sdc = ShapeDrawerConfig(menuBatch)
    val drawer = sdc.getDrawer()

    var baseColor = ColorPalette.of("darkGray")

    @Suppress("NewApi")
    override fun render(delta: Float) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        when {
            Gdx.input.isKeyJustPressed(Input.Keys.I) -> { baseColor = baseColor.incr() }
            Gdx.input.isKeyJustPressed(Input.Keys.D) -> { baseColor = baseColor.decr() }
            Gdx.input.isKeyJustPressed(Input.Keys.R) -> { baseColor = baseColor.incrR() }
            Gdx.input.isKeyJustPressed(Input.Keys.G) -> { baseColor = baseColor.incrG() }
            Gdx.input.isKeyJustPressed(Input.Keys.B) -> { baseColor = baseColor.incrB() }
            Gdx.input.isKeyJustPressed(Input.Keys.E) -> { baseColor = baseColor.decrR() }
            Gdx.input.isKeyJustPressed(Input.Keys.F) -> { baseColor = baseColor.decrG() }
            Gdx.input.isKeyJustPressed(Input.Keys.V) -> { baseColor = baseColor.decrB() }
        }

        val w3cColors = ColorPalette.w3cExtGrayBlack()

        menuBatch.use {
            w3cColors.values.forEachIndexed { index, colorPalette ->
                drawer.filledRectangle(40f, 400f - (20f * index), 40f, 20f, colorPalette.color())
            }
        }
        menuBatch.use {
            w3cColors.entries.forEachIndexed { index, colorPaletteEntry ->
                assets[FontAssets.OpenSansRegular].drawLabel(menuBatch, Vector2(60f, 410f - (20f * index)), colorPaletteEntry.key, colorPaletteEntry.value.inv().color())
            }
        }


        /* spectrum
        menuBatch.use {
            drawer.filledRectangle(100f, 100f, 20f, 20f, baseColor.color())
            drawer.filledRectangle(140f, 100f, 20f, 20f, baseColor.comp().color())
            drawer.filledRectangle(180f, 100f, 20f, 20f, baseColor.triad().first.color())
            drawer.filledRectangle(220f, 100f, 20f, 20f, baseColor.triad().second.color())
            baseColor.spectrum().forEachIndexed { index, colorPalette ->
                drawer.filledRectangle(100f + (40f * index), 50f, 20f, 20f, colorPalette.color())
            }
        }
*/
        engine.update(delta)
    }

    override fun hide() {
    }

    override fun show() {
        FontAssets.values().forEach { assets.load(it) }
        assets.finishLoading()
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