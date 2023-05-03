package river.exertion.kcop.profile.settings

import kotlinx.serialization.Serializable

@Serializable
data class ProfileSetting(val key : ProfileSettingSelection, var value : String, val display: Boolean? = null)
