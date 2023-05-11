package river.exertion.kcop.profile.settings

data class ProfileSettingOption(val optionValue : String, val optionLabel : String, val optionAction : () -> Unit)