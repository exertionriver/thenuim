package river.exertion.kcop.system.messaging.messages

import river.exertion.kcop.system.immersionTimer.ImmersionTimerState

data class ImmersionTimerMessage(val timerId : String?, val toState : ImmersionTimerState?)
