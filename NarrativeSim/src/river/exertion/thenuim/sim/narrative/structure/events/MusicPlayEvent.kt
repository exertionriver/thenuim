package river.exertion.thenuim.sim.narrative.structure.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import river.exertion.thenuim.sim.narrative.asset.NarrativeAsset
import river.exertion.thenuim.sim.narrative.asset.NarrativeStateAsset
import river.exertion.thenuim.sim.narrative.structure.NarrativeState
import river.exertion.thenuim.view.layout.AudioView

@Serializable
@SerialName("playMusic")
class MusicPlayEvent(
    override val type: String = "",
    override val musicFile: String = "",
) : Event(), IMusicEvent {

    override fun execEvent(previousEvent : Event?) {
        NarrativeStateAsset.currentNarrativeStateAsset.narrativeState.setBlockFlag("exec_${id!!}", NarrativeState.EventFiredValue)
        AudioView.playMusic(NarrativeAsset.currentNarrativeAsset.narrative.music[musicFile]!!)
    }

    override fun resolveEvent(currentEvent: Event?) {
        if (currentEvent == null) {
            NarrativeStateAsset.currentNarrativeStateAsset.narrativeState.setBlockFlag("resolve_${id!!}", NarrativeState.EventFiredValue)
            AudioView.stopMusic()
        }
    }
}