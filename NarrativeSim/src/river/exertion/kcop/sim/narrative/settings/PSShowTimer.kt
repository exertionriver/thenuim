package river.exertion.kcop.sim.narrative.settings

import river.exertion.kcop.ecs.switchboard.ImmersionTimerSwitchboard
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.messaging.Switchboard
import river.exertion.kcop.profile.ProfilePackage
import river.exertion.kcop.profile.ProfilePackage.ProfileBridge
import river.exertion.kcop.profile.messaging.ProfileComponentMessage
import river.exertion.kcop.profile.settings.ProfileSetting
import river.exertion.kcop.profile.settings.ProfileSettingOption

object PSShowTimer : ProfileSetting {

    override val selectionKey = "showTimer"

    override val selectionLabel = "Show Timer"

    override val display: Boolean = true

    override val options = listOf (
        ProfileSettingOption("showProfile","Profile") {
            MessageChannelHandler.send(ProfileBridge, ProfileComponentMessage(ProfileComponentMessage.ProfileMessageType.ReplaceCumlTimer))
        },
        ProfileSettingOption("showImmersion","Immersion") {
            Switchboard.executeAction(ImmersionTimerSwitchboard.ShowImmersionTimer.switchboardTag)
        }
    )
}