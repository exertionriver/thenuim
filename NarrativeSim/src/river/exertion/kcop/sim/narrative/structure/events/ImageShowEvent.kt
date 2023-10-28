package river.exertion.kcop.sim.narrative.structure.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import river.exertion.kcop.sim.narrative.asset.NarrativeAsset
import river.exertion.kcop.sim.narrative.asset.NarrativeStateAsset
import river.exertion.kcop.sim.narrative.structure.NarrativeState
import river.exertion.kcop.view.layout.displayViewLayout.DVLayoutHandler

@Serializable
@SerialName("showImage")
class ImageShowEvent(
    override val type: String = "",
    override val imageFile: String = "",
    override val layoutPaneTag: String = ""
) : Event(), IImageEvent {

    override fun execEvent(previousEvent : Event?) {
        NarrativeStateAsset.currentNarrativeStateAsset.narrativeState.setBlockFlag("exec_${id!!}", NarrativeState.EventFiredValue)
        DVLayoutHandler.showImage(layoutPaneTag, NarrativeAsset.currentNarrativeAsset.narrative.textures[imageFile]!!)
    }

    override fun resolveEvent(currentEvent: Event?) {
        if (currentEvent == null) {
            NarrativeStateAsset.currentNarrativeStateAsset.narrativeState.setBlockFlag("resolve_${id!!}", NarrativeState.EventFiredValue)
            DVLayoutHandler.hideImage(layoutPaneTag)
        }
    }
}