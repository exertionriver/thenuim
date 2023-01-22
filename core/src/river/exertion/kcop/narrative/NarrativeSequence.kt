package river.exertion.kcop.narrative

import kotlinx.serialization.Serializable
import river.exertion.kcop.system.Id

@Serializable
data class NarrativeSequence(
    override val id: String = "",
    val sequenceBlocks : MutableList<NarrativeSequenceBlock> = mutableListOf()
) : Id {
    var currentSequenceNumber = 0L

    fun init() {
        currentSequenceNumber = sequenceBlocks.minOf { it.sequenceNumber }
    }

    fun next() {
        currentSequenceNumber = sequenceBlocks.filter { it.sequenceNumber > currentSequenceNumber }.minOfOrNull { it.sequenceNumber } ?: currentSequenceNumber
    }

    fun prev() {
        currentSequenceNumber = sequenceBlocks.filter { it.sequenceNumber < currentSequenceNumber }.maxOfOrNull { it.sequenceNumber } ?: currentSequenceNumber
    }

    fun currentText() = sequenceBlocks.firstOrNull { it.sequenceNumber == currentSequenceNumber }?.narrativeText ?: "<narrativeText not found at currentSequenceNumber>"

}