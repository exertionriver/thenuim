package river.exertion.kcop.ecs

import com.badlogic.gdx.assets.AssetManager
import river.exertion.kcop.ecs.messaging.EngineComponentMessage
import river.exertion.kcop.ecs.messaging.EngineEntityMessage
import river.exertion.kcop.ecs.messaging.ImmersionTimerMessage
import river.exertion.kcop.messaging.Id
import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.plugin.IPackage

object ECSPackage : IPackage {

    override var id = Id.randomId()

    override var name = this::class.simpleName.toString()

    override fun loadChannels() {
        MessageChannelHandler.addChannel(MessageChannel(EngineComponentBridge, EngineComponentMessage::class))
        MessageChannelHandler.addChannel(MessageChannel(EngineEntityBridge, EngineEntityMessage::class))
        MessageChannelHandler.addChannel(MessageChannel(ImmersionTimerBridge, ImmersionTimerMessage::class))
    }

    override fun loadAssets(assetManager: AssetManager) {}

    override fun loadMenus() {}

    override fun loadSystems() {}

    override fun dispose() {
        EngineHandler.dispose()
    }

    const val EngineComponentBridge = "EngineComponentBridge"
    const val EngineEntityBridge = "EngineEntityBridge"
    const val ImmersionTimerBridge = "ImmersionTimerBridge"

}