package river.exertion.kcop.narrative.structure

import kotlinx.serialization.Serializable
import river.exertion.kcop.Id
import river.exertion.kcop.system.ecs.component.ImmersionLocation
import river.exertion.kcop.system.ecs.component.ImmersionStatus
import river.exertion.kcop.system.immersionTimer.ImmersionTimer

@Serializable
class NarrativeImmersion(
        override var id : String = Id.randomId(),

        var location : ImmersionLocation? = null,

        var blockImmersionTimers : MutableMap<String, String> = mutableMapOf(),

        var flags : MutableList<ImmersionStatus> = mutableListOf()

) : Id {
    fun immersionBlockId() = location?.immersionBlockId ?: UnknownBlockId
    fun cumlImmersionTime() = if (location != null) location!!.cumlImmersionTime else ImmersionTimer.CumlTimeZero

    companion object {
        const val UnknownBlockId = "unknown"
    }
}
