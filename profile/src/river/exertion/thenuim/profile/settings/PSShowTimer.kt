package river.exertion.thenuim.profile.settings

import river.exertion.thenuim.messaging.MessageChannelHandler
import river.exertion.thenuim.profile.ProfileKlop.ProfileBridge
import river.exertion.thenuim.profile.messaging.ProfileComponentMessage

object PSShowTimer : ProfileSetting {

    override val selectionKey = "showTimer"

    override val selectionLabel = "Timer"

    override val display: Boolean = true

    const val ShowProfile = "showProfile"

    override val options = mutableListOf (
        ProfileSettingOption(ShowProfile,"Profile") {
            MessageChannelHandler.send(ProfileBridge, ProfileComponentMessage(ProfileComponentMessage.ProfileMessageType.ReplaceCumlTimer))
        },
    )

}