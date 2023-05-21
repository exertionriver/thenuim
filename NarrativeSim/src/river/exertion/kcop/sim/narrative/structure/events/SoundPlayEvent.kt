package river.exertion.kcop.sim.narrative.structure.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import river.exertion.kcop.sim.narrative.asset.NarrativeAsset
import river.exertion.kcop.sim.narrative.asset.NarrativeStateAsset
import river.exertion.kcop.sim.narrative.structure.NarrativeState
import river.exertion.kcop.view.layout.AudioView

@Serializable
@SerialName("playSound")
class SoundPlayEvent(
    override val type: String = "",
    override val trigger: String = "",
    override val musicFile: String = "",
) : Event(), ISoundEvent, ITriggerEvent {

    override fun execEvent(previousEvent : Event?) {
        NarrativeStateAsset.currentNarrativeStateAsset.narrativeState.setPersistFlag(id!!, NarrativeState.EventFiredValue)
        AudioView.playSound(NarrativeAsset.currentNarrativeAsset.narrative.sounds[musicFile]!!)
    }
}
