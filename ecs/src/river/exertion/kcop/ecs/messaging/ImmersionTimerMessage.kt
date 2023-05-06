package river.exertion.kcop.ecs.messaging

import river.exertion.kcop.plugin.immersionTimer.ImmersionTimerState
import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler

data class ImmersionTimerMessage(val timerId : String?, val toState : ImmersionTimerState?)
