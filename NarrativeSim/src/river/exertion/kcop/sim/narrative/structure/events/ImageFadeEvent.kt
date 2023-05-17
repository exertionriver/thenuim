package river.exertion.kcop.sim.narrative.structure.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import river.exertion.kcop.sim.narrative.asset.NarrativeAsset
import river.exertion.kcop.sim.narrative.asset.NarrativeStateAsset
import river.exertion.kcop.sim.narrative.structure.NarrativeState
import river.exertion.kcop.sim.narrative.view.DVLayoutHandler

@Serializable
@SerialName("fadeImage")
class ImageFadeEvent(
    override val type: String = "",
    override val imageFile: String = "",
    override val layoutPaneIdx: String = ""
) : Event(), IImageEvent {

    override fun execEvent(previousEvent : Event?) {
        if (previousEvent == null) {
            NarrativeStateAsset.currentNarrativeStateAsset.narrativeState.setBlockFlag("exec_${id!!}", NarrativeState.EventFiredValue)
            DVLayoutHandler.fadeImageIn(layoutPaneIdx.toIntOrNull() ?: throw Exception("non-integer idx for DVPane!"), NarrativeAsset.currentNarrativeAsset.narrative.textures[imageFile]!!.asset)
        } else if ( (previousEvent as IImageEvent).imageFile != imageFile ) {
            NarrativeStateAsset.currentNarrativeStateAsset.narrativeState.setBlockFlag("exec_${id!!}", NarrativeState.EventFiredValue)
            DVLayoutHandler.showImage(layoutPaneIdx.toIntOrNull() ?: throw Exception("non-integer idx for DVPane!"), NarrativeAsset.currentNarrativeAsset.narrative.textures[imageFile]!!.asset)
        }
    }

    override fun resolveEvent(currentEvent: Event?) {
        if (currentEvent == null) {
            NarrativeStateAsset.currentNarrativeStateAsset.narrativeState.setBlockFlag("resolve_${id!!}", NarrativeState.EventFiredValue)
            DVLayoutHandler.fadeImageOut(layoutPaneIdx.toIntOrNull() ?: throw Exception("non-integer idx for DVPane!"), NarrativeAsset.currentNarrativeAsset.narrative.textures[imageFile]!!.asset)
        }
    }
}