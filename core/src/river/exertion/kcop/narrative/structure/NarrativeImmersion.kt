package river.exertion.kcop.narrative.structure

import kotlinx.serialization.Serializable
import river.exertion.kcop.Id
import river.exertion.kcop.system.immersionTimer.ImmersionTimer

@Serializable
class NarrativeImmersion(
        override var id : String = genId(),

        var location : ImmersionLocation? = null,

        var blockImmersionTimers : MutableMap<String, String> = mutableMapOf(),

        var flags : MutableList<ImmersionStatus> = mutableListOf()

) : Id {
    fun immersionBlockId() = location?.immersionBlockId ?: UnknownBlockId
    fun cumlImmersionTime() = if (location != null) location!!.cumlImmersionTime else ImmersionTimer.CumlTimeZero

    fun eventFired(id : String) = flags.any { it.key == id && it.value == EventFiredValue }

    companion object {
        const val UnknownBlockId = "unknown"
        const val EventFiredValue = "fired"
        const val FlagSetValue = "set"

        fun genId(profileId : String? = null, narrativeId : String? = null) = "${profileId ?: Id.randomId()}_${narrativeId ?: Id.randomId()}"
    }
}