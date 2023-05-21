package river.exertion.kcop.profile.settings

import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.profile.ProfilePackage.ProfileBridge
import river.exertion.kcop.profile.messaging.ProfileComponentMessage

object PSShowTimer : ProfileSetting {

    override val selectionKey = "showTimer"

    override val selectionLabel = "Show Timer"

    override val display: Boolean = true

    const val ShowProfile = "showProfile"

    override val options = mutableListOf (
        ProfileSettingOption(ShowProfile,"Profile") {
            MessageChannelHandler.send(ProfileBridge, ProfileComponentMessage(ProfileComponentMessage.ProfileMessageType.ReplaceCumlTimer))
        },
    )

}