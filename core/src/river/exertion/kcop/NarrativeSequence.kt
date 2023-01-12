package river.exertion.kcop

import kotlinx.serialization.Serializable

@Serializable
data class NarrativeSequence(
    override val id: String = "",
    val sequenceBlocks : MutableList<NarrativeSequenceBlock> = mutableListOf()
) : Id {
}