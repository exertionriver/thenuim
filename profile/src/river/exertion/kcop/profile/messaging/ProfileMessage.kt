package river.exertion.kcop.profile.messaging

import river.exertion.kcop.profile.Profile
import river.exertion.kcop.profile.settings.ProfileSetting

data class ProfileMessage(val profileMessageType : ProfileMessageType, val profile : Profile? = null, val immersionId : String? = null, val settings : MutableList<ProfileSetting>? = null) {

    enum class ProfileMessageType {
        ReplaceCumlTimer, UpdateSettings, UpdateProfile, Inactivate
    }
}
