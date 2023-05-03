package river.exertion.kcop.profile.settings

import river.exertion.kcop.messaging.Switchboard
import river.exertion.kcop.plugin.switchboard.IImmersionPluginSwitchboard.HideCompletionStatus
import river.exertion.kcop.plugin.switchboard.IImmersionPluginSwitchboard.ShowCompletionStatus

object PSCompStatus : ProfileSettingSelection {

    override val selectionKey = "completionStatus"

    override val selectionLabel = "Show Completion Status"

    override val options = listOf (
        ProfileSettingOption("Show") {
            Switchboard.executeAction(ShowCompletionStatus.switchboardTag)

        },
        ProfileSettingOption("Hide") {
            Switchboard.executeAction(HideCompletionStatus.switchboardTag)

        }
    )
}