package river.exertion.kcop.plugin

import river.exertion.kcop.asset.IAsset
import river.exertion.kcop.asset.IAssets
import river.exertion.kcop.plugin.immersionTimer.ImmersionTimerPair

interface IImmersionPackage : IDisplayPackage {

    fun timerPair() : ImmersionTimerPair

    var immersionAssets : IAssets
    var selectedImmersionAsset : IAsset
    var currentImmersionAsset : IAsset
}