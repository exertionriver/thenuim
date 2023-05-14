package river.exertion.kcop.sim.narrative

import com.badlogic.gdx.scenes.scene2d.Actor
import river.exertion.kcop.asset.AssetManagerHandler
import river.exertion.kcop.asset.AssetManagerHandler.lfhr
import river.exertion.kcop.asset.IAsset
import river.exertion.kcop.asset.IAssets
import river.exertion.kcop.ecs.switchboard.ImmersionTimerSwitchboard
import river.exertion.kcop.ecs.system.SystemHandler
import river.exertion.kcop.messaging.Id
import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.plugin.IImmersionPackage
import river.exertion.kcop.plugin.immersionTimer.ImmersionTimerPair
import river.exertion.kcop.sim.narrative.asset.NarrativeAsset
import river.exertion.kcop.sim.narrative.asset.NarrativeAssetLoader
import river.exertion.kcop.sim.narrative.asset.NarrativeAssets
import river.exertion.kcop.sim.narrative.menu.LoadNarrativeMenu
import river.exertion.kcop.sim.narrative.menu.NarrativeMenu
import river.exertion.kcop.sim.narrative.messaging.NarrativeMenuDataMessage
import river.exertion.kcop.sim.narrative.messaging.NarrativeMessage
import river.exertion.kcop.sim.narrative.system.NarrativeTextSystem
import river.exertion.kcop.sim.narrative.view.DVLayoutHandler
import river.exertion.kcop.view.ViewPackage
import river.exertion.kcop.view.menu.DisplayViewMenuHandler
import river.exertion.kcop.view.menu.MainMenu
import river.exertion.kcop.view.messaging.DisplayViewMessage
import river.exertion.kcop.view.messaging.menuParams.ActionParam

object NarrativePackage : IImmersionPackage {
    override var id = Id.randomId()

    override var name = this::class.simpleName.toString()

    override var immersionAssets : IAssets = NarrativeAssets
    override var selectedImmersionAsset : IAsset = NarrativeAsset()
    override var currentImmersionAsset : IAsset = NarrativeAsset()

    override fun loadChannels() {
        MessageChannelHandler.addChannel(MessageChannel(NarrativeBridge, NarrativeMessage::class))
        MessageChannelHandler.addChannel(MessageChannel(NarrativeMenuDataBridge, NarrativeMenuDataMessage::class))
    }

    override fun loadAssets() {
        AssetManagerHandler.assets.setLoader(NarrativeAsset::class.java, NarrativeAssetLoader(lfhr))
    }

    override fun loadSystems() {
        SystemHandler.pooledEngine.addSystem(NarrativeTextSystem())
    }

    override fun loadMenus() {
        DisplayViewMenuHandler.addMenu(LoadNarrativeMenu)
        DisplayViewMenuHandler.addMenu(NarrativeMenu)

        MainMenu.assignableNavs.add(
            ActionParam("Narrative >", {
                DisplayViewMenuHandler.currentMenuTag = NarrativeMenu.tag
                MessageChannelHandler.send(ViewPackage.DisplayViewBridge, DisplayViewMessage(DisplayViewMessage.DisplayViewMessageType.Rebuild) )
        }) )
    }

    override fun build() : Actor = DVLayoutHandler.buildLayout()

    override fun inputProcessor() = NarrativeInputProcessor

    override fun timerPair() = ImmersionTimerPair()

    override fun showImmersionTimer(immersionTimerPair: ImmersionTimerPair) = ImmersionTimerSwitchboard.showImmersionTimer(timerPair())

    override fun dispose() {

    }

    const val NarrativeBridge = "NarrativeBridge"
    const val NarrativeMenuDataBridge = "NarrativeMenuDataBridge"
}