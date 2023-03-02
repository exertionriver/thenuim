package river.exertion.kcop.system.profile

import kotlinx.serialization.Serializable

@Serializable
data class Status(val key : String, var value : Float)