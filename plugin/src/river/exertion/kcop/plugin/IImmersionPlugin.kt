package river.exertion.kcop.plugin

import river.exertion.kcop.ecs.immersionTimer.ImmersionTimerPair
import river.exertion.kcop.messaging.SwitchboardEntry

interface IImmersionPlugin : IPlugin {

    fun timerPair() : ImmersionTimerPair

}