package river.exertion.kcop.system.profile

import kotlinx.serialization.Serializable

@Serializable
data class ProfileSetting(val key : String, var value : String, val display: Boolean? = null)
