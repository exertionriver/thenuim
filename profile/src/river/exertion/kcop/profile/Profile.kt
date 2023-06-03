package river.exertion.kcop.profile

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import river.exertion.kcop.asset.character.NameTypes
import river.exertion.kcop.asset.Id
import river.exertion.kcop.asset.immersionTimer.ImmersionTimer
import river.exertion.kcop.profile.settings.PSCompStatus
import river.exertion.kcop.profile.settings.PSCompStatus.settingByKey
import river.exertion.kcop.profile.settings.PSShowTimer
import river.exertion.kcop.profile.settings.ProfileSetting
import river.exertion.kcop.profile.settings.ProfileSettingEntry

@Serializable
data class Profile(
        var id : String = Id.randomId(),
        var name : String = genName(),
        private var sCumlTime : String = ImmersionTimer.CumlTimeZero,
        var settingEntries : MutableList<ProfileSettingEntry> = defaultSettings()
    ) {

    @Transient
    var cumlTimer : ImmersionTimer = ImmersionTimer().apply {
        this.setPastStartTime(ImmersionTimer.inMilliseconds(sCumlTime))
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
