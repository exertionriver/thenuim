package river.exertion.kcop.profile.settings

import river.exertion.kcop.view.layout.StatusView

object PSCompStatus : ProfileSetting {

    override val selectionKey = "completionStatus"

    override val selectionLabel = "Completion Status"

    override val display: Boolean = true

    override val options = mutableListOf (
        ProfileSettingOption("show","Show") {
            StatusView.showStatusByFilter(selectionKey)
        },
        ProfileSettingOption("hide", "Hide") {
            StatusView.hideStatusByFilter(selectionKey)
        }
    )
}