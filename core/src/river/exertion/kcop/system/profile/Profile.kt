package river.exertion.kcop.system.profile

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import river.exertion.kcop.Id
import river.exertion.kcop.system.ecs.component.PSShowTimer
import river.exertion.kcop.system.ecs.component.ProfileSetting
import river.exertion.kcop.system.immersionTimer.ImmersionTimer

@Serializable
data class Profile(
    override var id : String = Id.randomId(),
    var name : String,
    var cumlTime : String? = null,
    var currentImmersionId : String? = null,
    var settings : MutableList<ProfileSetting>? = defaultSettings()
    ) : Id {

    @Transient
    var currentImmersionName : String? = null

    @Transient
    var currentImmersionBlockId : String? = null

    fun currentImmersionBlockId() = if (currentImmersionBlockId != null) "@ $currentImmersionBlockId " else ""

    @Transient
    var currentImmersionTime : String? = null

    fun currentImmersionTime() = currentImmersionTime ?: ImmersionTimer.CumlTimeZero

    fun cumlTime() = if (cumlTime != null) cumlTime!! else ImmersionTimer.CumlTimeZero

    fun profileInfo() : List<String> {
        val returnList = mutableListOf<String>()

        returnList.add("name: $name")
        returnList.add("cuml. time: ${cumlTime()}")

        if (currentImmersionName != null) {
            returnList.add("current immersion: $currentImmersionName ${currentImmersionBlockId()}[${currentImmersionTime()}]")
        }

        return returnList.toList()
    }

    companion object {
        fun defaultSettings() : MutableList<ProfileSetting> {
            return mutableListOf(
                ProfileSetting(PSShowTimer.selectionKey, PSShowTimer.options[0].optionValue)
            )
        }
    }
}
