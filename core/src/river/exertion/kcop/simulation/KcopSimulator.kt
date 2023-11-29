package river.exertion.kcop.simulation

import GdxDesktopTestBehavior
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import ktx.app.KtxScreen
import river.exertion.kcop.asset.*
import river.exertion.kcop.automation.AutomationKlop
import river.exertion.kcop.base.KcopBase
import river.exertion.kcop.ecs.EngineHandler
import river.exertion.kcop.ecs.klop.IECSKlop
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.sim.colorPalette.ColorPaletteDisplayKlop
import river.exertion.kcop.sim.narrative.NarrativeKlop
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.ViewKlop.KcopBridge
import river.exertion.kcop.view.asset.FreeTypeFontAssets
import river.exertion.kcop.view.klop.IDisplayViewKlop
import river.exertion.kcop.view.layout.*
import river.exertion.kcop.view.messaging.KcopSimulationMessage


class KcopSimulator : Telegraph, KtxScreen {

    private val coreDisplayViewPackages = mutableListOf(
        NarrativeKlop,
        ColorPaletteDisplayKlop,
    )

    init {
        coreDisplayViewPackages.forEach {
            PluginAssets.values.add(PluginAsset(it::javaClass.get()))
        }

        FreeTypeFontAssets.reload()

        AssetManagerHandler.assets.setLoader(PluginAsset::class.java, PluginAssetLoader(AssetManagerHandler.lfhr))
        PluginAssets.reload()
        PluginAssets.values.filter {it.assetStatus == null}.forEach { (it.assetData() as IDisplayViewKlop).load() }
        PluginAssets.values.removeIf { it.assetStatus != null }

        AutomationKlop.load()

        MessageChannelHandler.enableReceive(KcopBridge, this)
    }

    private var currentIDisplayViewKlopIdx = 0
    private fun currentIDisplayViewKlop() = PluginAssets.get()[currentIDisplayViewKlopIdx].assetDataTyped()
    private fun nextIDisplayViewKlopIdx() {
        currentIDisplayViewKlopIdx++
        if (currentIDisplayViewKlopIdx >= PluginAssets.get().size)
            currentIDisplayViewKlopIdx = 0
    }

    override fun render(delta: Float) {

        KcopBase.render(delta, EngineHandler.engine)

    }

    override fun hide() {
    }


    override fun show() {
        val pluginArgIdx = AppArgHandler.appArgs.keys.toList().indexOf("-plugin")
        val pluginArgValue = if (pluginArgIdx >= 0) AppArgHandler.appArgs.values.toList()[pluginArgIdx] else null
        val loadPlugin = PluginAssets.byName(pluginArgValue)

        currentIDisplayViewKlopIdx = if (loadPlugin != null) PluginAssets.values.indexOf(loadPlugin as IAsset) else 0

        if (AppArgHandler.appArgs.keys.contains("-displayView")) {
            DisplayViewMode.currentDVMode = DisplayViewMode.DisplayViewScreen
        } else if (AppArgHandler.appArgs.keys.contains("-displayFull")) {
            DisplayViewMode.currentDVMode = DisplayViewMode.DisplayFullScreen
        }

        ViewLayout.build(KcopBase.stage)

        currentIDisplayViewKlop().showView()
        LogView.addLog("kcop loaded with plugin:${currentIDisplayViewKlop().tag}")

        ButtonView.assignableButtons[5] = { GdxDesktopTestBehavior.testBehavior() }
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
        KcopBase.orthoCamera.viewportWidth = width.toFloat()
        KcopBase.orthoCamera.viewportHeight = height.toFloat()
        KcopBase.stage.viewport.update(width, height)
    }

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                (MessageChannelHandler.isType(KcopBridge, msg.message) ) -> {
                    val kcopSimulationMessage: KcopSimulationMessage = MessageChannelHandler.receiveMessage(KcopBridge, msg.extraInfo)

                    when (kcopSimulationMessage.kcopMessageType) {
                        KcopSimulationMessage.KcopMessageType.DisplayFullScreen -> {
                            ViewLayout.screenTransition(currentDisplayViewKlop = currentIDisplayViewKlop(), DisplayViewMode.DisplayFullScreen)
                            AudioView.playSound(KcopSkin.uiSounds[KcopSkin.UiSounds.Swoosh])
                        }
                        KcopSimulationMessage.KcopMessageType.DisplayViewScreen -> {
                            ViewLayout.screenTransition(currentDisplayViewKlop = currentIDisplayViewKlop(), DisplayViewMode.DisplayViewScreen)
                            AudioView.playSound(KcopSkin.uiSounds[KcopSkin.UiSounds.Swoosh])
                        }
                        KcopSimulationMessage.KcopMessageType.KcopScreen -> {
                            ViewLayout.screenTransition(currentDisplayViewKlop = currentIDisplayViewKlop(), DisplayViewMode.KcopScreen)
                            AudioView.playSound(KcopSkin.uiSounds[KcopSkin.UiSounds.Swoosh])
                        }
                        KcopSimulationMessage.KcopMessageType.NextPlugin -> {
                            currentIDisplayViewKlop().hideView()
                            nextIDisplayViewKlopIdx()

                            PluginAssets.get().filter { it.assetDataTyped() != currentIDisplayViewKlop() }.forEach {
                                if (it.assetDataTyped()::class.java.interfaces.contains(IECSKlop::class.java))
                                    (it.assetDataTyped() as IECSKlop).unloadSystems()
                            }

                            if (currentIDisplayViewKlop()::class.java.interfaces.contains(IECSKlop::class.java))
                                (currentIDisplayViewKlop() as IECSKlop).loadSystems()

                            currentIDisplayViewKlop().showView()
                            LogView.addLog("kcop plugin loaded:${currentIDisplayViewKlop().tag}")
                        }
                    }

                    return true
                }
            }
        }
        return false
    }

    override fun dispose() {
        coreDisplayViewPackages.forEach {
            it.dispose()
        }

        super.dispose()
    }
}