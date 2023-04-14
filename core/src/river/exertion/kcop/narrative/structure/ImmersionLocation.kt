package river.exertion.kcop.narrative.structure

import kotlinx.serialization.Serializable
import river.exertion.kcop.system.immersionTimer.ImmersionTimer

@Serializable
data class ImmersionLocation(var immersionBlockId : String? = null, var cumlImmersionTime : String? = ImmersionTimer.CumlTimeZero)
