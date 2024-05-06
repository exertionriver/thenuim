package river.exertion.thenuim.profile.messaging

import river.exertion.thenuim.profile.Profile
import river.exertion.thenuim.profile.settings.ProfileSettingEntry

data class ProfileComponentMessage(val profileMessageType : ProfileMessageType, val profile : Profile? = null, val immersionId : String? = null, val settings : MutableList<ProfileSettingEntry>? = null) {

    enum class ProfileMessageType {
        ReplaceCumlTimer, Inactivate
    }
}
