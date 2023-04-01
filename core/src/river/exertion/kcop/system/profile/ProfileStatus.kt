package river.exertion.kcop.system.profile

import kotlinx.serialization.Serializable

@Serializable
data class ProfileStatus(val immersionName : String, val key : String, var value : Float? = null, val display: Boolean? = null)