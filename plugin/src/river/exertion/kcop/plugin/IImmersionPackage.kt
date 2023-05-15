package river.exertion.kcop.plugin

import river.exertion.kcop.asset.IAsset
import river.exertion.kcop.asset.IAssets
import river.exertion.kcop.plugin.immersionTimer.ImmersionTimerPair

interface IImmersionPackage : IKcopPackage, IDisplayPackage {

    fun timerPair() : ImmersionTimerPair
    fun showImmersionTimer(immersionTimerPair : ImmersionTimerPair)

    var immersionAssets : IAssets
    var selectedImmersionAsset : IAsset
    var currentImmersionAsset : IAsset
    var currentImmersionStateAsset : IAsset
}