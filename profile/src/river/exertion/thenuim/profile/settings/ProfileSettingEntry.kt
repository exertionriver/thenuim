package river.exertion.thenuim.profile.settings

import kotlinx.serialization.Serializable

@Serializable
data class ProfileSettingEntry(val profileSettingSelectionKey: String, var profileSettingOptionValue : String) {

    companion object {
        fun MutableList<ProfileSettingEntry>.optionBySelectionKey(selectionKey : String) = this.firstOrNull { it.profileSettingSelectionKey == selectionKey }?.profileSettingOptionValue

        fun MutableList<ProfileSettingEntry>.updateSetting(key : String, value : String) {
            if ( this.firstOrNull { it.profileSettingSelectionKey == key } == null ) {
                this.add(ProfileSettingEntry(key, value))
            } else {
                this.first { it.profileSettingSelectionKey == key }.profileSettingOptionValue = value
            }
        }
    }
}
