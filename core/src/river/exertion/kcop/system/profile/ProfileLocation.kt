package river.exertion.kcop.system.profile

import kotlinx.serialization.Serializable

@Serializable
data class ProfileLocation(val immersionName : String, var immersionBlockId : String? = null, var cumlImmersionTime : String? = null)
