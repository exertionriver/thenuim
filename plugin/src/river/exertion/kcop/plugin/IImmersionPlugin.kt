package river.exertion.kcop.plugin

import river.exertion.kcop.plugin.immersionTimer.ImmersionTimerPair

interface IImmersionPlugin : IPlugin {

    fun timerPair() : ImmersionTimerPair

}