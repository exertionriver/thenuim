package river.exertion.kcop.profile.settings

import river.exertion.kcop.ecs.switchboard.IImmersionPluginSwitchboard.ShowImmersionTimer
import river.exertion.kcop.messaging.Switchboard

object PSShowTimer : ProfileSetting {

    override val selectionKey = "showTimer"

    override val selectionLabel = "Show Timer"

    override val display: Boolean = true

    override val options = listOf (
        ProfileSettingOption("showProfile","Profile") {
//            MessageChannelHandler.send(ProfileBridge, ProfileMessage(ProfileMessage.ProfileMessageType.ReplaceCumlTimer))
        },
        ProfileSettingOption("showImmersion","Immersion") {
            Switchboard.executeAction(ShowImmersionTimer.switchboardTag)
        }
    )
}