package river.exertion.kcop.narrative.structure

import kotlinx.serialization.Serializable
import river.exertion.kcop.Id

@Serializable
data class NarrativeBlock(
    override var id: String = "",
    val narrativeText: String = "",
    val sequenceNumber: Long = 0L
) : Id()