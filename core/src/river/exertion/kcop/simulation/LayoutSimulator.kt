package river.exertion.kcop.simulation

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
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

class LayoutSimulator(private val menuBatch: Batch,
                      private val assets: AssetManager,
                      private val menuStage: Stage,
                      private val menuCamera: OrthographicCamera) : KtxScreen {

    val engine = PooledEngine().apply { SystemManager.init(this) }
    val observer = Observer.instantiate(engine)
    val sdc = ShapeDrawerConfig(menuBatch)
    val drawer = sdc.getDrawer()

    val firstWidth = 21 * menuCamera.viewportWidth / 34f
    val firstHeight = 21 * menuCamera.viewportHeight / 21f

    val secondWidth = 13 * menuCamera.viewportWidth / 34f
    val secondHeight = 13 * menuCamera.viewportHeight / 21f

    val thirdWidth = 8 * menuCamera.viewportWidth / 34f
    val thirdHeight = 8 * menuCamera.viewportHeight / 21f

    val fourthWidth = 5 * menuCamera.viewportWidth / 34f
    val fourthHeight = 5 * menuCamera.viewportHeight / 21f

    val fifthWidth = 3 * menuCamera.viewportWidth / 34f
    val fifthHeight = 3 * menuCamera.viewportHeight / 21f

    val sixthWidth = 2 * menuCamera.viewportWidth / 34f
    val sixthHeight = 2 * menuCamera.viewportHeight / 21f

    val seventhWidth = menuCamera.viewportWidth / 34f
    val seventhHeight = menuCamera.viewportHeight / 21f

    val firstColor = ColorPalette.Color402
    val secondColor = ColorPalette.Color635

    @Suppress("NewApi")
    override fun render(delta: Float) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        menuBatch.use {
            drawer.filledRectangle(0f, 0f, firstWidth, firstHeight, firstColor.color())
            drawer.filledRectangle(firstWidth, thirdHeight, secondWidth, secondHeight, firstColor.comp().color())
            drawer.filledRectangle(firstWidth + fourthWidth, 0f, thirdWidth, thirdHeight, firstColor.triad().first.color())
            drawer.filledRectangle(firstWidth, 0f, fourthWidth, fourthHeight, firstColor.triad().second.color())
            drawer.filledRectangle(firstWidth, fourthHeight, fifthWidth, fifthHeight, secondColor.color())
            drawer.filledRectangle(firstWidth + fifthWidth, fourthHeight + seventhHeight, sixthWidth, sixthHeight, secondColor.comp().color())
            drawer.filledRectangle(firstWidth + fifthWidth + seventhWidth, fourthHeight, seventhWidth, seventhHeight, secondColor.triad().first.color())
            drawer.filledRectangle(firstWidth + fifthWidth, fourthHeight, seventhWidth, seventhHeight, secondColor.triad().second.color())
        }
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