package river.exertion.kcop.simulation.colorPalette

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Button
import ktx.actors.onClick
import ktx.app.KtxScreen
import river.exertion.kcop.assets.*
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.AiHintMessage
import river.exertion.kcop.system.messaging.messages.KcopMessage
import river.exertion.kcop.system.messaging.messages.TextViewMessage


class ColorPaletteSimulator(private val stage: Stage,
                            private val assetManagerHandler: AssetManagerHandler,
                            private val orthoCamera: OrthographicCamera) : KtxScreen {

    val colorPaletteLayout = ColorPaletteLayout(orthoCamera.viewportWidth, orthoCamera.viewportHeight)

    @Suppress("NewApi")
    override fun render(delta: Float) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        stage.act(Gdx.graphics.deltaTime)
        stage.draw()

        when {
            Gdx.input.isKeyJustPressed(Input.Keys.R) -> { colorPaletteLayout.colorBaseIncrR() }
            Gdx.input.isKeyJustPressed(Input.Keys.G) -> { colorPaletteLayout.colorBaseIncrG() }
            Gdx.input.isKeyJustPressed(Input.Keys.B) -> { colorPaletteLayout.colorBaseIncrB() }
            Gdx.input.isKeyJustPressed(Input.Keys.E) -> { colorPaletteLayout.colorBaseDecrR() }
            Gdx.input.isKeyJustPressed(Input.Keys.F) -> { colorPaletteLayout.colorBaseDecrG() }
            Gdx.input.isKeyJustPressed(Input.Keys.V) -> { colorPaletteLayout.colorBaseDecrB()}

            Gdx.input.isKeyJustPressed(Input.Keys.UP) -> { colorPaletteLayout.colorBaseIncr() }
            Gdx.input.isKeyJustPressed(Input.Keys.DOWN) -> { colorPaletteLayout.colorBaseDecr() }
            Gdx.input.isKeyJustPressed(Input.Keys.LEFT) -> { colorPaletteLayout.colorSamplePrev() }
            Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) -> { colorPaletteLayout.colorSampleNext() }
        }
    }

    override fun hide() {
    }

    override fun show() {
        Gdx.input.inputProcessor = stage

        stage.addActor(colorPaletteLayout.createSampleSwatchesCtrl())
        stage.addActor(colorPaletteLayout.createBaseSwatchesCtrl())
        stage.addActor(colorPaletteLayout.createCompSwatchesCtrl())
        stage.addActor(colorPaletteLayout.createTriadFirstSwatchesCtrl())
        stage.addActor(colorPaletteLayout.createTriadSecondSwatchesCtrl())
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
        assetManagerHandler.dispose()
        colorPaletteLayout.dispose()
    }

}