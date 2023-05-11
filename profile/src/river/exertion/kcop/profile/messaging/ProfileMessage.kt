package river.exertion.kcop.profile.messaging

import river.exertion.kcop.profile.Profile
import river.exertion.kcop.profile.settings.ProfileSettingEntry

data class ProfileMessage(val profileMessageType : ProfileMessageType, val profile : Profile? = null, val immersionId : String? = null, val settings : MutableList<ProfileSettingEntry>? = null) {

    enum class ProfileMessageType {
        ReplaceCumlTimer, UpdateSettings, UpdateProfile, Inactivate
    }
}
