package river.exertion.kcop.system.profile

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import river.exertion.kcop.Id
import river.exertion.kcop.assets.AssetManagerHandler
import river.exertion.kcop.narrative.character.NameTypes
import river.exertion.kcop.system.immersionTimer.ImmersionTimer
import river.exertion.kcop.system.profile.settings.PSCompStatus
import river.exertion.kcop.system.profile.settings.PSShowTimer

@Serializable
data class Profile(
    override var id : String = Id.randomId(),
    var name : String = genName(),
    var cumlTime : String = ImmersionTimer.CumlTimeZero,
    var currentImmersionId : String? = AssetManagerHandler.NoNarrativeLoaded,
    var settings : MutableList<ProfileSetting> = defaultSettings()
    ) : Id {

    @Transient
    var currentImmersionName : String? = null

    @Transient
    var currentImmersionBlockId : String? = null

    fun currentImmersionBlockId() = if (currentImmersionBlockId != null) "@ $currentImmersionBlockId " else ""

    @Transient
    var currentImmersionTime : String? = null

    fun currentImmersionTime() = currentImmersionTime ?: ImmersionTimer.CumlTimeZero

    fun profileInfo() : List<String> {
        val returnList = mutableListOf<String>()

        returnList.add("name: $name")
        returnList.add("cuml. time: $cumlTime")

        if (currentImmersionName != null) {
            returnList.add("current immersion: $currentImmersionName ${currentImmersionBlockId()}[${currentImmersionTime()}]")
        }

        return returnList.toList()
    }

    companion object {
        fun defaultSettings() : MutableList<ProfileSetting> {
            return mutableListOf(
                ProfileSetting(PSShowTimer.selectionKey, PSShowTimer.options[0].optionValue),
                ProfileSetting(PSCompStatus.selectionKey, PSCompStatus.options[0].optionValue)
            )
        }

        fun genName() = NameTypes.COMMON.nextName()
    }
}
