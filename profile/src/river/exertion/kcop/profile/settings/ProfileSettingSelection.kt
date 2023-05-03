package river.exertion.kcop.profile.settings

interface ProfileSettingSelection {
    val selectionKey : String
    val selectionLabel : String
    val options : List<ProfileSettingOption>

    fun optionByValue(value : String) = options.firstOrNull { it.optionLabel == value } ?: throw Exception("asdf")
}