package river.exertion.kcop.simulation

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g3d.*
import com.badlogic.gdx.scenes.scene2d.Stage
import ktx.app.KtxScreen
import ktx.scene2d.*
import river.exertion.kcop.assets.*
import river.exertion.kcop.simulation.view.ViewLayout
import river.exertion.kcop.system.ecs.EngineHandler
import river.exertion.kcop.system.view.ViewInputProcessor


class ViewSimulator(private val stage: Stage,
                    private val engineHandler: EngineHandler,
                    private val assetManagerHandler: AssetManagerHandler,
                    private val orthoCamera: OrthographicCamera) : KtxScreen {

    val layout = ViewLayout(orthoCamera.viewportWidth, orthoCamera.viewportHeight)

    @Suppress("NewApi")
    override fun render(delta: Float) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        stage.act(Gdx.graphics.deltaTime)
        stage.draw()

        engineHandler.engine.update(delta)
    }

    override fun hide() {
    }

    override fun show() {
        val inputMultiplexer = InputMultiplexer()
        inputMultiplexer.addProcessor(ViewInputProcessor())
        inputMultiplexer.addProcessor(stage)
        Gdx.input.inputProcessor = inputMultiplexer

        stage.addActor(layout.createDisplayViewCtrl())
        stage.addActor(layout.createTextViewCtrl(assetManagerHandler.assets[TextureAssets.KoboldA]))
        stage.addActor(layout.createLogViewCtrl(assetManagerHandler.assets[TextureAssets.KoboldA], assetManagerHandler.assets[TextureAssets.KoboldB]))
        stage.addActor(layout.createStatusViewCtrl(assetManagerHandler.assets[TextureAssets.KoboldA]))
        stage.addActor(layout.createMenuViewCtrl(assetManagerHandler.assets[TextureAssets.KoboldA], assetManagerHandler.assets[TextureAssets.KoboldB], assetManagerHandler.assets[TextureAssets.KoboldC]))
        stage.addActor(layout.createInputsViewCtrl(assetManagerHandler.assets[TextureAssets.KoboldA], assetManagerHandler.assets[TextureAssets.KoboldB], assetManagerHandler.assets[TextureAssets.KoboldC]))
        stage.addActor(layout.createAiViewCtrl(assetManagerHandler.assets[TextureAssets.BlueSphere], assetManagerHandler.assets[TextureAssets.BlueSphere], assetManagerHandler.assets[TextureAssets.BlueSphere]))
        stage.addActor(layout.createPauseViewCtrl(assetManagerHandler.assets[TextureAssets.KoboldA], assetManagerHandler.assets[TextureAssets.KoboldB], assetManagerHandler.assets[TextureAssets.KoboldC]))

        //experiments for border
//        stage.addActor(Image(NinePatch(assets[TextureAssets.KoboldA])).apply { this.x = 0f; this.y = 0f; this.width = 10f; this.height = orthoCamera.viewportHeight })
//        stage.addActor(Image(NinePatch(assets[TextureAssets.KoboldA])).apply { this.x = ViewType.firstWidth(orthoCamera.viewportWidth) - 10; this.y = 0f; this.width = 10f; this.height = orthoCamera.viewportHeight })

//        layout.displayViewCtrl.recreate()


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
        layout.dispose()
    }
}