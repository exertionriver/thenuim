package river.exertion.kcop.system.profile

import kotlinx.serialization.Serializable
import river.exertion.kcop.Id

@Serializable
data class Profile(
    var name : String,
    var currentImmersionName : String? = null,
    val locations : MutableList<ProfileLocation>? = null,
    val statuses : MutableList<ProfileStatus>? = null
) : Id() {

    fun immersionBlockId(immersionName : String? = currentImmersionName) = locations?.firstOrNull { it.immersionName == immersionName }?.immersionBlockId

    fun immersionCumlTime(immersionName : String? = currentImmersionName) = locations?.firstOrNull { it.immersionName == immersionName }?.cumlImmersionTime

    fun profileInfo() : List<String> {
        val returnList = mutableListOf<String>()

        returnList.add("name: $name")

        if (currentImmersionName != null && immersionBlockId() != null)
            returnList.add("current: $currentImmersionName: ${immersionBlockId()}")

        if (locations?.isNotEmpty() == true) returnList.add("\nlocations:")

        val locationListMaxSize = locations?.size?.coerceAtMost(8) ?: 0

        locations?.sortedByDescending { it.cumlImmersionTime }?.subList(0, locationListMaxSize)?.forEach { location ->
                returnList.add("${location.immersionName}: ${location.immersionBlockId} [${location.cumlImmersionTime}]")
        }

        if (statuses?.isNotEmpty() == true) returnList.add("\nstatuses:")

        val statusListMaxSize = statuses?.size?.coerceAtMost(8) ?: 0

        statuses?.sortedByDescending { it.value }?.subList(0, statusListMaxSize)?.forEach { status ->
            returnList.add("${status.immersionName}: ${status.key} [${status.value}]")
        }

        if ( returnList.isEmpty() ) returnList.add("no profile info found")

        return returnList.toList()
    }
}
