package river.exertion.kcop.sim.narrative.structure

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import river.exertion.kcop.messaging.Id
import river.exertion.kcop.plugin.immersionTimer.ImmersionTimer
import river.exertion.kcop.plugin.immersionTimer.ImmersionTimerPair

@Serializable
class NarrativeState(
    override var id : String = genId(),

    var location : ImmersionLocation? = null,

    private var sBlockCumlImmersionTimers : MutableMap<String, String> = mutableMapOf(),

    var flags : MutableList<ImmersionStatus> = mutableListOf()

) : Id {

    fun immersionBlockId() = location?.immersionBlockId ?: UnknownBlockId

    var cumlImmersionTimer : ImmersionTimer
        get() = ImmersionTimer().apply { this.setPastStartTime(ImmersionTimer.inMilliseconds(location?.cumlImmersionTime ?: ImmersionTimer.CumlTimeZero)) }
        set(value) { location?.cumlImmersionTime = value.immersionTime() }

    //persisted as cuml timer
    var blockCumlImmersionTimers : MutableMap<String, ImmersionTimer>
        get() = sBlockCumlImmersionTimers.mapValues { (_, values) -> ImmersionTimer().apply { this.setPastStartTime(ImmersionTimer.inMilliseconds(values)) } }.toMutableMap()
        set(value) { sBlockCumlImmersionTimers = value.mapValues { (_,values) -> values.immersionTime() }.toMutableMap() }

    //not persisted, cleared when moving from block to block
    @Transient
    var blockFlags : MutableList<ImmersionStatus> = mutableListOf()

    fun setBlockFlag(key : String, value : String) {
        if (blockFlags.any { it.key == key }) {
            blockFlags.first { it.key == key }.value = value
        } else {
            blockFlags.add(ImmersionStatus(key, value))
        }
    }

    fun blockEventFired(id : String) = blockFlags.any { it.key == id && it.value == EventFiredValue }

    fun setPersistFlag(key : String, value : String) {
        if (flags.any { it.key == key }) {
            flags.first { it.key == key }.value = value
        } else {
            flags.add(ImmersionStatus(key, value))
        }
    }

    fun persistEventFired(id : String) = flags.any { it.key == id && it.value == EventFiredValue }

    fun addToCounter(key : String, value : String) {
        if (flags.any { it.key == key && (it.value?.toIntOrNull() is Int) }) {
            flags.first { it.key == key }.value =
                    (flags.first { it.key == key }.value!!.toInt() + (value.toIntOrNull() ?: 0)).toString()
        } else {
            flags.add(ImmersionStatus(key, value))
        }
    }

    @Suppress("NewApi")
    fun unsetPersistFlag(key : String) {
        flags.removeIf { it.key == key }
    }

    companion object {
        const val UnknownBlockId = "unknown"
        const val EventFiredValue = "fired"
        const val FlagSetValue = "set"

        fun genId(profileId : String? = null, narrativeId : String? = null) = "${profileId ?: Id.randomId()}_${narrativeId ?: Id.randomId()}"
    }
}
