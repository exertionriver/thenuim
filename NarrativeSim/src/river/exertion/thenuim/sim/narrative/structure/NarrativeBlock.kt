package river.exertion.thenuim.sim.narrative.structure

import kotlinx.serialization.Serializable
import river.exertion.thenuim.base.Id

@Serializable
data class NarrativeBlock(
    var id: String = Id.randomId(),
    val narrativeText: MutableList<String> = mutableListOf(),
    val displayText: MutableList<String> = mutableListOf(),
    val fontSize: String = "",
    val sequenceNumber: Long = 0L
) {
    fun toTextString(): String = narrativeText.reduceOrNull { acc, s -> acc.plus(s) } ?: ""
    fun toDisplayString(): String = displayText.reduceOrNull { acc, s -> acc.plus(s) } ?: ""
}