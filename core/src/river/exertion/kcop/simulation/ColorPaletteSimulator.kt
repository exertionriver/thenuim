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
    var colorKindIdx = 0
    var colorKindMinIdx = 0
    var colorKindMaxIdx = ColorPalette.w3cColors().size - 1
    var colorKind = ColorPalette.w3cColors()[colorKindIdx]

    val firstColorColumn = 40f
    val firstTextColumn = 60f
    val secondColorColumn = 140f
    val secondTextColumn = 160f
    val thirdColorColumn = 240f
    val thirdTextColumn = 260f
    val fourthColorColumn = 340f
    val fourthTextColumn = 360f
    val fifthColorColumn = 440f
    val fifthTextColumn = 460f

    val firstColorRow = 400f
    val firstTextRow = 410f

    val colorSwatchHeight = 20f
    val colorSwatchWidth = 80f

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

            Gdx.input.isKeyJustPressed(Input.Keys.LEFT) -> {
                if (colorKindIdx == colorKindMinIdx) colorKindIdx = colorKindMaxIdx else colorKindIdx--
                colorKind = ColorPalette.w3cColors()[colorKindIdx]
            }
            Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) -> {
                if (colorKindIdx == colorKindMaxIdx) colorKindIdx = colorKindMinIdx else colorKindIdx++
                colorKind = ColorPalette.w3cColors()[colorKindIdx]
            }
        }

        menuBatch.use {
            colorKind.values.forEachIndexed { index, colorPalette ->
                drawer.filledRectangle(firstColorColumn, firstColorRow - (colorSwatchHeight * index), colorSwatchWidth, colorSwatchHeight, colorPalette.color())
            }
        }
        menuBatch.use {
            colorKind.entries.forEachIndexed { index, colorPaletteEntry ->
                assets[FontAssets.OpenSansRegular].drawLabel(menuBatch, Vector2(firstTextColumn, firstTextRow - (colorSwatchHeight * index)), colorPaletteEntry.key, colorPaletteEntry.value.inv().color())
            }
        }

        menuBatch.use {
            drawer.filledRectangle(secondColorColumn, firstColorRow, colorSwatchWidth, colorSwatchHeight, baseColor.color())
            drawer.filledRectangle(thirdColorColumn, firstColorRow, colorSwatchWidth, colorSwatchHeight, baseColor.comp().color())
            drawer.filledRectangle(fourthColorColumn, firstColorRow, colorSwatchWidth, colorSwatchHeight, baseColor.triad().first.color())
            drawer.filledRectangle(fifthColorColumn, firstColorRow, colorSwatchWidth, colorSwatchHeight, baseColor.triad().second.color())

            baseColor.spectrum().forEachIndexed { index, colorPalette ->
                drawer.filledRectangle(secondColorColumn, firstColorRow - (colorSwatchHeight * (index + 2)), colorSwatchWidth, colorSwatchHeight, colorPalette.color())
            }
            baseColor.comp().spectrum().forEachIndexed { index, colorPalette ->
                drawer.filledRectangle(thirdColorColumn, firstColorRow - (colorSwatchHeight * (index + 2)), colorSwatchWidth, colorSwatchHeight, colorPalette.color())
            }
            baseColor.triad().first.spectrum().forEachIndexed { index, colorPalette ->
                drawer.filledRectangle(fourthColorColumn, firstColorRow - (colorSwatchHeight * (index + 2)), colorSwatchWidth, colorSwatchHeight, colorPalette.color())
            }
            baseColor.triad().second.spectrum().forEachIndexed { index, colorPalette ->
                drawer.filledRectangle(fifthColorColumn, firstColorRow - (colorSwatchHeight * (index + 2)), colorSwatchWidth, colorSwatchHeight, colorPalette.color())
            }
        }

        menuBatch.use {
            assets[FontAssets.OpenSansRegular].drawLabel(menuBatch, Vector2(secondTextColumn, firstTextRow), baseColor.tags()[0], baseColor.inv().color())
            assets[FontAssets.OpenSansRegular].drawLabel(menuBatch, Vector2(thirdTextColumn, firstTextRow), baseColor.comp().tags()[0], baseColor.comp().inv().color())
            assets[FontAssets.OpenSansRegular].drawLabel(menuBatch, Vector2(fourthTextColumn, firstTextRow), baseColor.triad().first.tags()[0], baseColor.triad().first.inv().color())
            assets[FontAssets.OpenSansRegular].drawLabel(menuBatch, Vector2(fifthTextColumn, firstTextRow), baseColor.triad().second.tags()[0], baseColor.triad().second.inv().color())

            baseColor.spectrum().forEachIndexed { index, colorPalette ->
                assets[FontAssets.OpenSansRegular].drawLabel(menuBatch, Vector2(secondTextColumn, firstTextRow - (colorSwatchHeight * (index + 2))), colorPalette.tags()[0], colorPalette.inv().color())
            }
            baseColor.comp().spectrum().forEachIndexed { index, colorPalette ->
                assets[FontAssets.OpenSansRegular].drawLabel(menuBatch, Vector2(thirdTextColumn, firstTextRow - (colorSwatchHeight * (index + 2))), colorPalette.tags()[0], colorPalette.inv().color())
            }
            baseColor.triad().first.spectrum().forEachIndexed { index, colorPalette ->
                assets[FontAssets.OpenSansRegular].drawLabel(menuBatch, Vector2(fourthTextColumn, firstTextRow - (colorSwatchHeight * (index + 2))), colorPalette.tags()[0], colorPalette.inv().color())
            }
            baseColor.triad().second.spectrum().forEachIndexed { index, colorPalette ->
                assets[FontAssets.OpenSansRegular].drawLabel(menuBatch, Vector2(fifthTextColumn, firstTextRow - (colorSwatchHeight * (index + 2))), colorPalette.tags()[0], colorPalette.inv().color())
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