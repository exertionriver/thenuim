package river.exertion.kcop.profile.settings

import river.exertion.kcop.messaging.Switchboard
import river.exertion.kcop.ecs.switchboard.IImmersionPluginSwitchboard.HideCompletionStatus
import river.exertion.kcop.ecs.switchboard.IImmersionPluginSwitchboard.ShowCompletionStatus

object PSCompStatus : ProfileSetting {

    override val selectionKey = "completionStatus"

    override val selectionLabel = "Show Completion Status"

    override val display: Boolean = true

    override val options = listOf (
        ProfileSettingOption("show","Show") {
            Switchboard.executeAction(ShowCompletionStatus.switchboardTag)

        },
        ProfileSettingOption("hide", "Hide") {
            Switchboard.executeAction(HideCompletionStatus.switchboardTag)

        }
    )
}