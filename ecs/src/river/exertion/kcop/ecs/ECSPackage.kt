package river.exertion.kcop.ecs

import river.exertion.kcop.ecs.component.ImmersionTimerComponent
import river.exertion.kcop.ecs.entity.SubjectEntity
import river.exertion.kcop.ecs.messaging.EngineComponentMessage
import river.exertion.kcop.ecs.messaging.EngineEntityMessage
import river.exertion.kcop.ecs.messaging.ImmersionTimerMessage
import river.exertion.kcop.ecs.switchboard.ImmersionTimerSwitchboard
import river.exertion.kcop.ecs.switchboard.ImmersionTimerSwitchboard.showImmersionTimer
import river.exertion.kcop.messaging.*
import river.exertion.kcop.plugin.IInternalPackage
import river.exertion.kcop.plugin.immersionTimer.ImmersionTimerPair

object ECSPackage : IInternalPackage {

    override var id = Id.randomId()

    override var name = this::class.simpleName.toString()

    override fun loadChannels() {
        MessageChannelHandler.addChannel(MessageChannel(EngineComponentBridge, EngineComponentMessage::class))
        MessageChannelHandler.addChannel(MessageChannel(EngineEntityBridge, EngineEntityMessage::class))
        MessageChannelHandler.addChannel(MessageChannel(ImmersionTimerBridge, ImmersionTimerMessage::class))

        ImmersionTimerSwitchboard.addShowImmersionTimerStub()
    }

    override fun dispose() {
        EngineHandler.dispose()
    }

    const val EngineComponentBridge = "EngineComponentBridge"
    const val EngineEntityBridge = "EngineEntityBridge"
    const val ImmersionTimerBridge = "ImmersionTimerBridge"

}