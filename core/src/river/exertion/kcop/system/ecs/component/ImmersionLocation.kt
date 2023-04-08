package river.exertion.kcop.system.ecs.component

import kotlinx.serialization.Serializable
import river.exertion.kcop.system.immersionTimer.ImmersionTimer

@Serializable
data class ImmersionLocation(var immersionBlockId : String? = null, var cumlImmersionTime : String? = null) {

    fun cumlImmersionTime() = if (cumlImmersionTime != null) cumlImmersionTime else ImmersionTimer.CumlTimeZero
}
