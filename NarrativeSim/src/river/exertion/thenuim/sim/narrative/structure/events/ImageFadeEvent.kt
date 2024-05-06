package river.exertion.thenuim.sim.narrative.structure.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import river.exertion.thenuim.sim.narrative.asset.NarrativeAsset
import river.exertion.thenuim.sim.narrative.asset.NarrativeStateAsset
import river.exertion.thenuim.sim.narrative.structure.NarrativeState
import river.exertion.thenuim.view.layout.displayViewLayout.DVLayoutHandler

@Serializable
@SerialName("fadeImage")
class ImageFadeEvent(
    override val type: String = "",
    override val imageFile: String = "",
    override val layoutPaneTag: String = ""
) : Event(), IImageEvent {

    override fun execEvent(previousEvent : Event?) {
        if (previousEvent == null) {
            NarrativeStateAsset.currentNarrativeStateAsset.narrativeState.setBlockFlag("exec_${id!!}", NarrativeState.EventFiredValue)
            DVLayoutHandler.fadeImageIn(layoutPaneTag, NarrativeAsset.currentNarrativeAsset.narrative.textures[imageFile]!!)
        } else if ( (previousEvent as IImageEvent).imageFile != imageFile ) {
            NarrativeStateAsset.currentNarrativeStateAsset.narrativeState.setBlockFlag("exec_${id!!}", NarrativeState.EventFiredValue)
            DVLayoutHandler.showImage(layoutPaneTag, NarrativeAsset.currentNarrativeAsset.narrative.textures[imageFile]!!)
        }
    }

    override fun resolveEvent(currentEvent: Event?) {
        if (currentEvent == null) {
            NarrativeStateAsset.currentNarrativeStateAsset.narrativeState.setBlockFlag("resolve_${id!!}", NarrativeState.EventFiredValue)
            DVLayoutHandler.fadeImageOut(layoutPaneTag)
        }
    }
}