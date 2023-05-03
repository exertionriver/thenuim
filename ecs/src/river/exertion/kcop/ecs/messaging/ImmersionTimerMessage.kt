package river.exertion.kcop.ecs.messaging

import river.exertion.kcop.ecs.immersionTimer.ImmersionTimerState
import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler

data class ImmersionTimerMessage(val timerId : String?, val toState : ImmersionTimerState?) {

    init {
        MessageChannelHandler.addChannel(MessageChannel(ImmersionTimerBridge, this::class))
    }

    companion object {
        const val ImmersionTimerBridge = "ImmersionTimerBridge"
    }
}
