package river.exertion.kcop.profile.settings

import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.messaging.Switchboard
import river.exertion.kcop.plugin.switchboard.IImmersionPluginSwitchboard.ShowImmersionTimer
import river.exertion.kcop.profile.messaging.ProfileMessage
import river.exertion.kcop.profile.messaging.ProfileMessage.Companion.ProfileBridge

object PSShowTimer : ProfileSettingSelection {

    override val selectionKey = "showTimer"

    override val selectionLabel = "Show Timer"

    override val options = listOf (
        ProfileSettingOption("Profile") {
            MessageChannelHandler.send(ProfileBridge, ProfileMessage(ProfileMessage.ProfileMessageType.ReplaceCumlTimer))
        },
        ProfileSettingOption("Immersion") {
            Switchboard.executeAction(ShowImmersionTimer.switchboardTag)
        }
    )
}