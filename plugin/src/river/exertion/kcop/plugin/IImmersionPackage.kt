package river.exertion.kcop.plugin

import river.exertion.kcop.plugin.immersionTimer.ImmersionTimerPair

interface IImmersionPackage : IKcopPackage, IDisplayPackage {

    fun timerPair() : ImmersionTimerPair
    fun showImmersionTimer(immersionTimerPair : ImmersionTimerPair)
}