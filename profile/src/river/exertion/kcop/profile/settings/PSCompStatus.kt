package river.exertion.kcop.profile.settings

import river.exertion.kcop.view.switchboard.ViewSwitchboard

object PSCompStatus : ProfileSetting {

    override val selectionKey = "completionStatus"

    override val selectionLabel = "Show Completion Status"

    override val display: Boolean = true

    override val options = listOf (
        ProfileSettingOption("show","Show") {
            ViewSwitchboard.showCompletionStatus()
        },
        ProfileSettingOption("hide", "Hide") {
            ViewSwitchboard.hideCompletionStatus()
        }
    )
}