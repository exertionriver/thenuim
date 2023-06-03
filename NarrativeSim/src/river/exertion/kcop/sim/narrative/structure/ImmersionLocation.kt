package river.exertion.kcop.sim.narrative.structure

import kotlinx.serialization.Serializable
import river.exertion.kcop.asset.immersionTimer.ImmersionTimer

@Serializable
data class ImmersionLocation(var immersionBlockId : String? = null, var cumlImmersionTime : String? = ImmersionTimer.CumlTimeZero)
