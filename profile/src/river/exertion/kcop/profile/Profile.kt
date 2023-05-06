package river.exertion.kcop.profile

import kotlinx.serialization.Serializable
import river.exertion.kcop.asset.character.NameTypes
import river.exertion.kcop.messaging.Id
import river.exertion.kcop.plugin.immersionTimer.ImmersionTimer
import river.exertion.kcop.profile.settings.PSCompStatus
import river.exertion.kcop.profile.settings.PSShowTimer
import river.exertion.kcop.profile.settings.ProfileSetting

@Serializable
data class Profile(
    override var id : String = Id.randomId(),
    var name : String = genName(),
    var cumlTime : String = ImmersionTimer.CumlTimeZero,
//    var currentImmersionId : String = NoImmersionLoaded,
    var settings : MutableList<ProfileSetting> = defaultSettings()
    ) : Id {

//    @Transient
//    var currentImmersionName : String? = null

//    @Transient
//    var currentImmersionBlockId : String? = null

//    fun currentImmersionBlockId() = if (currentImmersionBlockId != null) "@ $currentImmersionBlockId " else ""

//    @Transient
//    var currentImmersionTime : String? = null

//    fun currentImmersionTime() = currentImmersionTime ?: ImmersionTimer.CumlTimeZero

    fun profileInfo() : List<String> {
        val returnList = mutableListOf<String>()

        returnList.add("name: $name")
        returnList.add("cuml. time: $cumlTime")

  //      if (currentImmersionName != null) {
 //           returnList.add("current immersion: $currentImmersionName ${currentImmersionBlockId()}[${currentImmersionTime()}]")
//        }

        return returnList.toList()
    }

    //update kcop with current settings, including setting log timers
    fun execSettings() {
        settings.forEach { it.key.optionByValue(it.value).optionAction() }
    }

    companion object {
        fun defaultSettings() : MutableList<ProfileSetting> {
            return mutableListOf(
                ProfileSetting(PSShowTimer, PSShowTimer.options[0].optionLabel),
                ProfileSetting(PSCompStatus, PSCompStatus.options[0].optionLabel)
            )
        }

        fun genName() = NameTypes.COMMON.nextName()
    }
}
