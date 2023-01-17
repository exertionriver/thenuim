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
import river.exertion.kcop.Layout
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

    val layout = Layout(menuCamera.viewportWidth, menuCamera.viewportHeight)

    val firstColor = ColorPalette.Color402
    val secondColor = ColorPalette.Color635

    @Suppress("NewApi")
    override fun render(delta: Float) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        menuBatch.use {
            drawer.filledRectangle(layout.displayViewRect(), firstColor.color())
            drawer.filledRectangle(layout.textViewRect(), firstColor.comp().color())
            drawer.filledRectangle(layout.logViewRect(), firstColor.triad().first.color())
            drawer.filledRectangle(layout.menuViewRect(), firstColor.triad().second.color())
            drawer.filledRectangle(layout.promptsViewRect(), secondColor.color())
            drawer.filledRectangle(layout.inputsViewRect(), secondColor.comp().color())
            drawer.filledRectangle(layout.aiViewRect(), secondColor.triad().first.color())
            drawer.filledRectangle(layout.pauseViewRect(), secondColor.triad().second.color())
        }

        menuBatch.use {
            (layout.textViewFirstRow().toInt() downTo layout.textViewLastRow().toInt() step layout.textViewRowHeight().toInt()).forEach { row ->
                assets[FontAssets.OpenSansRegular].drawLabel(menuBatch, Vector2(layout.textViewFirstCol(), row - layout.textViewRowHeight()), "text$row", secondColor.comp().inv().color())
            }
            (layout.logViewFirstRow().toInt() downTo layout.logViewLastRow().toInt() step layout.logViewRowHeight().toInt()).forEach { row ->
                assets[FontAssets.OpenSansRegular].drawLabel(menuBatch, Vector2(layout.logViewFirstCol(),row - layout.logViewRowHeight()), "log$row", secondColor.triad().first.inv().color())
            }
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