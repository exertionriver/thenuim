package river.exertion.kcop.profile.settings

interface ProfileSetting {
    val selectionKey : String
    val selectionLabel : String
    val options : List<ProfileSettingOption>
    val display: Boolean

    fun optionByValue(value : String) = options.firstOrNull { it.optionValue == value } ?: throw Exception("optionByValue(): option value not found")

    fun MutableList<ProfileSetting>.settingByKey(key : String) = this.firstOrNull { it.selectionKey == key }

}