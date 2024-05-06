package river.exertion.thenuim.sim.narrative.structure.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import river.exertion.thenuim.sim.narrative.asset.NarrativeStateAsset
import river.exertion.thenuim.sim.narrative.structure.NarrativeState

@Serializable
@SerialName("zeroCounter")
class CounterZeroEvent(
    override val type: String = "",
    override val trigger: String = "",
    val counterKey: String = ""
) : Event(), ITriggerEvent {

    override fun execEvent(previousEvent : Event?) {
        NarrativeStateAsset.currentNarrativeStateAsset.narrativeState.setPersistFlag(id!!, NarrativeState.EventFiredValue)
        NarrativeStateAsset.currentNarrativeStateAsset.narrativeState.setPersistFlag(counterKey, (0).toString() )
    }
}
