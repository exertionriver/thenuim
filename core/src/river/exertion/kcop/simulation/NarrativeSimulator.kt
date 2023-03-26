package river.exertion.kcop.simulation

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g3d.*
import com.badlogic.gdx.scenes.scene2d.Stage
import ktx.app.KtxScreen
import ktx.scene2d.*
import river.exertion.kcop.*
import river.exertion.kcop.assets.*
import river.exertion.kcop.simulation.view.ViewLayout
import river.exertion.kcop.system.*
import river.exertion.kcop.system.ecs.EngineHandler
import river.exertion.kcop.system.ecs.component.NarrativeComponent
import river.exertion.kcop.system.ecs.component.NarrativeComponentNavStatusHandler.activate
import river.exertion.kcop.system.ecs.component.NarrativeComponentNavStatusHandler.inactivate
import river.exertion.kcop.system.ecs.entity.NarrativeEntity
import river.exertion.kcop.system.view.ViewInputProcessor


class NarrativeSimulator(private val batch: Batch,
                         private val stage: Stage,
                         private val orthoCamera: OrthographicCamera) : KtxScreen {

    val viewLayout = ViewLayout(orthoCamera.viewportWidth, orthoCamera.viewportHeight)
    val engineHandler = EngineHandler()
    val assetManagerHandler = AssetManagerHandler()

    var narrativesIdx = 0
    var narrativeEntityName = ""
    lateinit var narrativesBlock : MutableMap<NarrativeAsset, String>

    @Suppress("NewApi")
    override fun render(delta: Float) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        stage.act(Gdx.graphics.deltaTime)
        stage.draw()

        engineHandler.engine.update(delta)

        when {
            Gdx.input.isKeyJustPressed(Input.Keys.LEFT) -> {
                val prevNarrativesIdx = narrativesIdx
                narrativesIdx = (narrativesIdx - 1).coerceAtLeast(0)
                if (prevNarrativesIdx != narrativesIdx) {
                    //save place in previous narrative
                    narrativesBlock[narrativesBlock.keys.toList()[prevNarrativesIdx]] = engineHandler.getComponentFor<NarrativeComponent>(narrativeEntityName)!!.narrativeCurrBlockId()
                    //remove status line
                    engineHandler.getComponentFor<NarrativeComponent>(narrativeEntityName)!!.inactivate()
                    //load new narrative component
                    engineHandler.replaceComponent(narrativeEntityName, NarrativeComponent::class.java, null, NarrativeComponent.NarrativeComponentInit(narrativesBlock.keys.toList()[narrativesIdx], narrativesBlock.values.toList()[narrativesIdx]))
                }
            }
            Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) -> {
                val prevNarrativesIdx = narrativesIdx
                narrativesIdx = (narrativesIdx + 1).coerceAtMost(narrativesBlock.entries.size - 1)
                if (prevNarrativesIdx != narrativesIdx) {
                    //save place in previous narrative
                    narrativesBlock[narrativesBlock.keys.toList()[prevNarrativesIdx]] = engineHandler.getComponentFor<NarrativeComponent>(narrativeEntityName)!!.narrativeCurrBlockId()
                    //remove status line
                    engineHandler.getComponentFor<NarrativeComponent>(narrativeEntityName)!!.inactivate()
                    //load new narrative component
                    engineHandler.replaceComponent(narrativeEntityName, NarrativeComponent::class.java, null, NarrativeComponent.NarrativeComponentInit(narrativesBlock.keys.toList()[narrativesIdx], narrativesBlock.values.toList()[narrativesIdx]))
                }
            }
        }
    }

    override fun hide() {
    }

    override fun show() {
        FreeTypeFontAssets.values().forEach { assetManagerHandler.assets.load(it) }
        TextureAssets.values().forEach { assetManagerHandler.assets.load(it) }
        NarrativeAssets.values().forEach { assetManagerHandler.assets.load(it) }
        assetManagerHandler.assets.finishLoading()

        val multiplexer = InputMultiplexer()
        multiplexer.addProcessor(ViewInputProcessor())
        multiplexer.addProcessor(stage)
        Gdx.input.inputProcessor = multiplexer

        val textFont = assetManagerHandler.assets[FreeTypeFontAssets.NotoSansSymbolsSemiBold]
        val displayFont = assetManagerHandler.assets[FreeTypeFontAssets.Immortal]

        stage.addActor(viewLayout.createDisplayViewCtrl(displayFont))
        stage.addActor(viewLayout.createTextViewCtrl(textFont, assetManagerHandler.assets[TextureAssets.KoboldA]))
        stage.addActor(viewLayout.createLogViewCtrl(textFont, assetManagerHandler.assets[TextureAssets.KoboldA], assetManagerHandler.assets[TextureAssets.KoboldB]))
        stage.addActor(viewLayout.createStatusViewCtrl(textFont, assetManagerHandler.assets[TextureAssets.KoboldA]))
        stage.addActor(viewLayout.createMenuViewCtrl(textFont, assetManagerHandler.assets[TextureAssets.KoboldA], assetManagerHandler.assets[TextureAssets.KoboldB], assetManagerHandler.assets[TextureAssets.KoboldC]))
        stage.addActor(viewLayout.createInputsViewCtrl(textFont, assetManagerHandler.assets[TextureAssets.KoboldA], assetManagerHandler.assets[TextureAssets.KoboldB], assetManagerHandler.assets[TextureAssets.KoboldC]))
        stage.addActor(viewLayout.createAiViewCtrl(textFont, assetManagerHandler.assets[TextureAssets.BlueSphere], assetManagerHandler.assets[TextureAssets.BlueSphere], assetManagerHandler.assets[TextureAssets.BlueSphere]))
        stage.addActor(viewLayout.createPauseViewCtrl(textFont, assetManagerHandler.assets[TextureAssets.KoboldA], assetManagerHandler.assets[TextureAssets.KoboldB], assetManagerHandler.assets[TextureAssets.KoboldC]))
        stage.addActor(viewLayout.createTextViewCtrl(textFont, assetManagerHandler.assets[TextureAssets.KoboldA]))

        narrativesBlock = NarrativeAssets.values().map { assetManagerHandler.assets[it] }.associateWith { it.narrative!!.firstBlock().id }.toMutableMap()

        narrativeEntityName = engineHandler.instantiateEntity(NarrativeEntity::class.java, NarrativeComponent.NarrativeComponentInit(narrativesBlock.keys.toList()[narrativesIdx], narrativesBlock.values.toList()[narrativesIdx]))
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
        viewLayout.dispose()
    }
}