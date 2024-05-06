package river.exertion.thenuim.sim.narrative.structure

import kotlinx.serialization.Serializable
import river.exertion.thenuim.asset.immersionTimer.ImmersionTimer

@Serializable
data class ImmersionLocation(var immersionBlockId : String? = null, var cumlImmersionTime : String? = ImmersionTimer.CumlTimeZero)
