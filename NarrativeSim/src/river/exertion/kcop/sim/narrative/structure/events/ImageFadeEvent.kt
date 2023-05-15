package river.exertion.kcop.sim.narrative.structure.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.sim.narrative.NarrativePackage
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
            NarrativePackage.currentNarrativeStateAsset.narrativeState.setBlockFlag("exec_${id!!}", NarrativeState.EventFiredValue)
            DVLayoutHandler.fadeImageIn(layoutPaneIdx.toIntOrNull() ?: throw Exception("non-integer idx for DVPane!"), NarrativePackage.currentNarrativeAsset.narrative.textures[imageFile]!!.asset)
        } else if ( (previousEvent as IImageEvent).imageFile != imageFile ) {
            NarrativePackage.currentNarrativeStateAsset.narrativeState.setBlockFlag("exec_${id!!}", NarrativeState.EventFiredValue)
            DVLayoutHandler.showImage(layoutPaneIdx.toIntOrNull() ?: throw Exception("non-integer idx for DVPane!"), NarrativePackage.currentNarrativeAsset.narrative.textures[imageFile]!!.asset)
        }
    }

    override fun resolveEvent(currentEvent: Event?) {
        if (currentEvent == null) {
            NarrativePackage.currentNarrativeStateAsset.narrativeState.setBlockFlag("resolve_${id!!}", NarrativeState.EventFiredValue)
            DVLayoutHandler.fadeImageOut(layoutPaneIdx.toIntOrNull() ?: throw Exception("non-integer idx for DVPane!"), NarrativePackage.currentNarrativeAsset.narrative.textures[imageFile]!!.asset)
        }
    }
}