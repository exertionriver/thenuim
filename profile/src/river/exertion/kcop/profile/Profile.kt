package river.exertion.kcop.profile

import kotlinx.serialization.Serializable
import river.exertion.kcop.asset.character.NameTypes
import river.exertion.kcop.messaging.Id
import river.exertion.kcop.plugin.immersionTimer.ImmersionTimer
import river.exertion.kcop.profile.settings.PSCompStatus
import river.exertion.kcop.profile.settings.PSCompStatus.settingByKey
import river.exertion.kcop.profile.settings.PSShowTimer
import river.exertion.kcop.profile.settings.ProfileSettingEntry
import river.exertion.kcop.profile.settings.ProfileSetting

@Serializable
data class Profile(
    override var id : String = Id.randomId(),
    var name : String = genName(),
    var cumlTime : String = ImmersionTimer.CumlTimeZero,
    var settingEntries : MutableList<ProfileSettingEntry> = defaultSettings()
    ) : Id {

    fun profileInfo() : List<String> {
        val returnList = mutableListOf<String>()

        returnList.add("name: $name")
        returnList.add("cuml. time: $cumlTime")

        return returnList.toList()
    }

    //update kcop with current settings, including setting log timers
    fun execSettings() {
        settingEntries.forEach { settingEntry -> availableSettings().settingByKey(settingEntry.profileSettingSelectionKey)?.optionByValue(settingEntry.profileSettingOptionValue)?.optionAction?.let { it -> it() } }
    }

    companion object {
        fun availableSettings() : MutableList<ProfileSetting> = mutableListOf(
            PSCompStatus,
            PSShowTimer
        )

        fun defaultSettings() : MutableList<ProfileSettingEntry> = availableSettings().map { profileSetting ->
            ProfileSettingEntry(profileSetting.selectionKey, profileSetting.options[0].optionValue)
        }.toMutableList()

        fun genName() = NameTypes.COMMON.nextName()
    }
}
