package river.exertion.thenuim.profile

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import river.exertion.thenuim.asset.character.NameTypes
import river.exertion.thenuim.base.Id
import river.exertion.thenuim.asset.immersionTimer.ImmersionTimer
import river.exertion.thenuim.profile.settings.PSCompStatus
import river.exertion.thenuim.profile.settings.PSCompStatus.settingByKey
import river.exertion.thenuim.profile.settings.PSShowTimer
import river.exertion.thenuim.profile.settings.ProfileSetting
import river.exertion.thenuim.profile.settings.ProfileSettingEntry

@Serializable
data class Profile(
    var id : String = Id.randomId(),
    var name : String = genName(),
    private var sCumlTime : String = ImmersionTimer.CumlTimeZero,
    var settingEntries : MutableList<ProfileSettingEntry> = defaultSettings()
    ) {

    @Transient
    var cumlTimer : ImmersionTimer = ImmersionTimer().apply {
        this.setPastStartTime(sCumlTime)
    }
        set(value) {
            field = value
            sCumlTime = value.immersionTime()
        }

    fun profileInfo() : List<String> {
        val returnList = mutableListOf<String>()

        returnList.add("name: $name")
        returnList.add("cuml. time: ${cumlTimer.immersionTime()}")

        return returnList.toList()
    }

    //update kcop with current settings, including setting log timers
    fun execSettings() {
        settingEntries.forEach { settingEntry -> availableSettings.settingByKey(settingEntry.profileSettingSelectionKey)?.optionByValue(settingEntry.profileSettingOptionValue)?.optionAction?.let { it -> it() } }
    }

    companion object {
        var availableSettings : MutableList<ProfileSetting> = mutableListOf(
            PSCompStatus, PSShowTimer
        )

        fun defaultSettings() : MutableList<ProfileSettingEntry> = availableSettings.map { profileSetting ->
            ProfileSettingEntry(profileSetting.selectionKey, profileSetting.options[0].optionValue)
        }.toMutableList()

        fun genName() = NameTypes.COMMON.nextName()
    }
}
