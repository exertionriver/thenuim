package river.exertion.thenuim.simulation

import GdxDesktopTestBehavior
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import ktx.app.KtxScreen
import river.exertion.thenuim.AppArgHandler
import river.exertion.thenuim.asset.IAsset
import river.exertion.thenuim.automation.AutomationLoPa
import river.exertion.thenuim.base.TnmBase
import river.exertion.thenuim.ecs.EngineHandler
import river.exertion.thenuim.ecs.IECSLoPa
import river.exertion.thenuim.messaging.MessageChannelHandler
import river.exertion.thenuim.profile.ProfileLoPa
import river.exertion.thenuim.sim.colorPalette.ColorPaletteDisplayLoPa
import river.exertion.thenuim.sim.narrative.NarrativeLoPa
import river.exertion.thenuim.view.TnmSkin
import river.exertion.thenuim.view.ViewLoPa
import river.exertion.thenuim.view.ViewLoPa.TnmBridge
import river.exertion.thenuim.view.layout.*
import river.exertion.thenuim.view.messaging.TnmSimMessage
import river.exertion.thenuim.view.plugin.DisplayViewPluginAsset
import river.exertion.thenuim.view.plugin.DisplayViewPluginAssets

//simulation of Thenuim
class TnmSim : Telegraph, KtxScreen {

    private val coreDisplayViewPackages = mutableListOf(
        NarrativeLoPa,
        ColorPaletteDisplayLoPa,
    )

    init {
        coreDisplayViewPackages.forEach {
            DisplayViewPluginAssets.values.add(DisplayViewPluginAsset(it::javaClass.get()))
        }

        ViewLoPa.load()
        ProfileLoPa.load()
        AutomationLoPa.load()

        DisplayViewPluginAssets.reload() // for external assets
        MessageChannelHandler.enableReceive(TnmBridge, this)
    }

    private var currentIDisplayViewLoPaIdx = 0
    private fun currentIDisplayViewLoPa() = DisplayViewPluginAssets.get()[currentIDisplayViewLoPaIdx].assetDataTyped()
    private fun nextIDisplayViewLoPaIdx() {
        currentIDisplayViewLoPaIdx++
        if (currentIDisplayViewLoPaIdx >= DisplayViewPluginAssets.get().size)
            currentIDisplayViewLoPaIdx = 0
    }

    override fun render(delta: Float) {

        TnmBase.render(delta, EngineHandler.engine)

    }

    override fun hide() {
    }


    override fun show() {
        val pluginArgIdx = AppArgHandler.appArgs.keys.toList().indexOf("-plugin")
        val pluginArgValue = if (pluginArgIdx >= 0) AppArgHandler.appArgs.values.toList()[pluginArgIdx] else null
        val loadPlugin = DisplayViewPluginAssets.byName(pluginArgValue)

        currentIDisplayViewLoPaIdx = if (loadPlugin != null) DisplayViewPluginAssets.values.indexOf(loadPlugin as IAsset) else 0

        if (AppArgHandler.appArgs.keys.contains("-displayCenter")) {
            DisplayViewMode.currentDVMode = DisplayViewMode.DisplayViewCenter
        } else if (AppArgHandler.appArgs.keys.contains("-displayFull")) {
            DisplayViewMode.currentDVMode = DisplayViewMode.DisplayViewFull
        }

        ViewLayout.build(TnmBase.stage)
        currentIDisplayViewLoPa().showView()

        LogView.addLog("Tnm loaded with plugin:${currentIDisplayViewLoPa().tag}")

        ButtonView.assignableButtons[5] = { GdxDesktopTestBehavior.testBehavior() }
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
        TnmBase.orthoCamera.viewportWidth = width.toFloat()
        TnmBase.orthoCamera.viewportHeight = height.toFloat()
        TnmBase.stage.viewport.update(width, height)
    }

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                (MessageChannelHandler.isType(TnmBridge, msg.message) ) -> {
                    val tnmSimMessage: TnmSimMessage = MessageChannelHandler.receiveMessage(TnmBridge, msg.extraInfo)

                    when (tnmSimMessage.tnmSimMessageType) {
                        TnmSimMessage.TnmSimMessageType.DisplayFullScreen -> {
                            ViewLayout.screenTransition(currentDisplayViewLoPa = currentIDisplayViewLoPa(), DisplayViewMode.DisplayViewFull)
                            AudioView.playSound(TnmSkin.uiSounds[TnmSkin.UiSounds.Swoosh])
                        }
                        TnmSimMessage.TnmSimMessageType.DisplayViewScreen -> {
                            ViewLayout.screenTransition(currentDisplayViewLoPa = currentIDisplayViewLoPa(), DisplayViewMode.DisplayViewCenter)
                            AudioView.playSound(TnmSkin.uiSounds[TnmSkin.UiSounds.Swoosh])
                        }
                        TnmSimMessage.TnmSimMessageType.DebugScreen -> {
                            ViewLayout.screenTransition(currentDisplayViewLoPa = currentIDisplayViewLoPa(), DisplayViewMode.DisplayViewWork)
                            AudioView.playSound(TnmSkin.uiSounds[TnmSkin.UiSounds.Swoosh])
                        }
                        TnmSimMessage.TnmSimMessageType.NextPlugin -> {
                            currentIDisplayViewLoPa().hideView()
                            nextIDisplayViewLoPaIdx()

                            DisplayViewPluginAssets.get().filter { it.assetDataTyped() != currentIDisplayViewLoPa() }.forEach {
                                if (it.assetDataTyped()::class.java.interfaces.contains(IECSLoPa::class.java))
                                    (it.assetDataTyped() as IECSLoPa).unloadSystems()
                            }

                            if (currentIDisplayViewLoPa()::class.java.interfaces.contains(IECSLoPa::class.java))
                                (currentIDisplayViewLoPa() as IECSLoPa).loadSystems()

                            currentIDisplayViewLoPa().showView()
                            LogView.addLog("Tnm plugin loaded:${currentIDisplayViewLoPa().tag}")
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