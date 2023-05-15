package river.exertion.kcop.sim.narrative.structure.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import river.exertion.kcop.sim.narrative.NarrativePackage
import river.exertion.kcop.sim.narrative.structure.NarrativeState
import river.exertion.kcop.view.layout.AudioView

@Serializable
@SerialName("fadeMusic")
class MusicFadeEvent(
    override val type: String = "",
    override val musicFile: String = "",
) : Event(), IMusicEvent {

    override fun execEvent(previousEvent : Event?) {
        if (previousEvent == null) {
            NarrativePackage.currentNarrativeStateAsset.narrativeState.setBlockFlag("exec_${id!!}", NarrativeState.EventFiredValue)
            AudioView.fadeInMusic(NarrativePackage.currentNarrativeAsset.narrative.music[musicFile]!!.asset)

        } else if ( (previousEvent as IMusicEvent).musicFile != musicFile ) {
            NarrativePackage.currentNarrativeStateAsset.narrativeState.setBlockFlag("exec_${id!!}", NarrativeState.EventFiredValue)
            AudioView.crossFadeMusic(NarrativePackage.currentNarrativeAsset.narrative.music[musicFile]!!.asset)
        }
    }

    override fun resolveEvent(currentEvent: Event?) {
        if (currentEvent == null) {
            NarrativePackage.currentNarrativeStateAsset.narrativeState.setBlockFlag("resolve_${id!!}", NarrativeState.EventFiredValue)
            AudioView.fadeOutMusic()
        }
    }
}