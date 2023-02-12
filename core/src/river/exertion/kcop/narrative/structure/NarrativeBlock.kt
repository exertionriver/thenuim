package river.exertion.kcop.narrative.structure

import kotlinx.serialization.Serializable
import river.exertion.kcop.Id

@Serializable
data class NarrativeBlock(
    override var id: String = "",
    val narrativeText: MutableList<String> = mutableListOf(),
    val sequenceNumber: Long = 0L
) : Id() {
    fun toTextString(): String = narrativeText.reduce { acc, s -> acc.plus(s) }
}