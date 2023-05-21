package river.exertion.kcop.sim.narrative.settings

import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.profile.ProfilePackage.ProfileBridge
import river.exertion.kcop.profile.messaging.ProfileComponentMessage
import river.exertion.kcop.profile.settings.ProfileSetting
import river.exertion.kcop.profile.settings.ProfileSettingOption
import river.exertion.kcop.sim.narrative.NarrativePackage.NarrativeBridge
import river.exertion.kcop.sim.narrative.messaging.NarrativeComponentMessage

object PSShowTimer : ProfileSetting {

    override val selectionKey = "showTimer"

    override val selectionLabel = "Show Timer"

    override val display: Boolean = true

    override val options = listOf (
        ProfileSettingOption("showProfile","Profile") {
            MessageChannelHandler.send(ProfileBridge, ProfileComponentMessage(ProfileComponentMessage.ProfileMessageType.ReplaceCumlTimer))
        },
        ProfileSettingOption("showImmersion","Immersion") {
            MessageChannelHandler.send(NarrativeBridge, NarrativeComponentMessage(NarrativeComponentMessage.NarrativeMessageType.ReplaceCumlTimer))
        },
        ProfileSettingOption("showImmersionBlock","Immersion Block") {
            MessageChannelHandler.send(NarrativeBridge, NarrativeComponentMessage(NarrativeComponentMessage.NarrativeMessageType.ReplaceBlockCumlTimer))
        }
    )
}