package river.exertion.kcop.profile.settings

import river.exertion.kcop.ecs.switchboard.ImmersionTimerSwitchboard
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.messaging.Switchboard
import river.exertion.kcop.profile.ProfilePackage.ProfileBridge
import river.exertion.kcop.profile.messaging.ProfileMessage

object PSShowTimer : ProfileSetting {

    override val selectionKey = "showTimer"

    override val selectionLabel = "Show Timer"

    override val display: Boolean = true

    override val options = listOf (
        ProfileSettingOption("showProfile","Profile") {
            MessageChannelHandler.send(ProfileBridge, ProfileMessage(ProfileMessage.ProfileMessageType.ReplaceCumlTimer))
        },
        ProfileSettingOption("showImmersion","Immersion") {
            Switchboard.executeAction(ImmersionTimerSwitchboard.ShowImmersionTimer.switchboardTag)
        }
    )
}