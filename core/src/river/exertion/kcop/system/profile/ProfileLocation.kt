package river.exertion.kcop.system.profile

import kotlinx.serialization.Serializable

@Serializable
data class ProfileLocation(val immersionName : String, var immersionBlockId : String, var cumlImmersionTime : String? = null)
