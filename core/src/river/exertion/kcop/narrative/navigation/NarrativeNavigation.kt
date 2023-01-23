package river.exertion.kcop.narrative.navigation

import kotlinx.serialization.Serializable
import river.exertion.kcop.system.Id

@Serializable
data class NarrativeNavigation(
    override val id: String = "",
    val navigationBlocks : MutableList<NarrativeNavigationBlock> = mutableListOf()
) : Id {
    var currentSequenceNumber = 0L

    fun init() {
        currentSequenceNumber = navigationBlocks.minOf { it.sequenceNumber }
    }

    fun next(nextIdx : Long) {
        currentSequenceNumber = navigationBlocks.firstOrNull { it.sequenceNumber == nextIdx }?.sequenceNumber ?: currentSequenceNumber
    }

    fun currentText() = navigationBlocks.firstOrNull { it.sequenceNumber == currentSequenceNumber }?.narrativeText ?: "<narrativeText not found at currentSequenceNumber>"

}