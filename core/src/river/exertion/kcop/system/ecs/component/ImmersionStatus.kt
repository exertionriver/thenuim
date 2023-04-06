package river.exertion.kcop.system.ecs.component

import kotlinx.serialization.Serializable

@Serializable
data class ImmersionStatus(val key : String, var value : String? = null, val display: Boolean? = null)