package river.exertion.kcop.sim.narrative

import com.badlogic.gdx.assets.AssetManager
import river.exertion.kcop.asset.AssetManagerHandler.lfhr
import river.exertion.kcop.messaging.Id
import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.plugin.IPlugin
import river.exertion.kcop.sim.narrative.asset.NarrativeAsset
import river.exertion.kcop.sim.narrative.asset.NarrativeAssetLoader
import river.exertion.kcop.sim.narrative.asset.NarrativeAssets
import river.exertion.kcop.sim.narrative.messaging.NarrativeMenuDataMessage
import river.exertion.kcop.sim.narrative.messaging.NarrativeMessage

class NarrativePackage : IPlugin {
    override var id = Id.randomId()

    override var name = this::class.simpleName.toString()

    var narrativeAssets = NarrativeAssets()

    override fun loadAssets(assetManager: AssetManager) {
        assetManager.setLoader(NarrativeAsset::class.java, NarrativeAssetLoader(lfhr))
        narrativeAssets.reload()
    }

    override fun loadChannels() {
        MessageChannelHandler.addChannel(MessageChannel(NarrativeBridge, NarrativeMessage::class))
        MessageChannelHandler.addChannel(MessageChannel(NarrativeMenuDataBridge, NarrativeMenuDataMessage::class))
    }

    companion object {
        const val NarrativeBridge = "NarrativeBridge"
        const val NarrativeMenuDataBridge = "NarrativeMenuDataBridge"
    }
}