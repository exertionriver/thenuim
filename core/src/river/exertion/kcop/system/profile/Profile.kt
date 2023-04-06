package river.exertion.kcop.system.profile

import kotlinx.serialization.Serializable
import river.exertion.kcop.Id
import river.exertion.kcop.system.immersionTimer.ImmersionTimer

@Serializable
data class Profile(
    override var id : String = Id.randomId(),
    var name : String,
    var cumlTime : String? = null,
    var currentImmersionId : String? = null,
    ) : Id {

    @Transient
    var currentImmersionName : String? = null

    @Transient
    var currentImmersionTime : String? = ImmersionTimer.zero()

    fun cumlTime() = if (cumlTime != null) cumlTime!! else ImmersionTimer.zero()

    fun profileInfo() : List<String> {
        val returnList = mutableListOf<String>()

        returnList.add("name: $name")
        returnList.add("cuml. time: ${cumlTime()}")

        if (currentImmersionName != null) {
            returnList.add("current immersion: $currentImmersionName [$currentImmersionTime]")
        }

        return returnList.toList()
    }
}
