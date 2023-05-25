package river.exertion.kcop.sim.narrative.structure

import kotlinx.serialization.Serializable
import river.exertion.kcop.messaging.Id

@Serializable
data class NarrativeBlock(
    override var id: String = Id.randomId(),
    val narrativeText: MutableList<String> = mutableListOf(),
    val displayText: MutableList<String> = mutableListOf(),
    val fontSize: String = "",
    val sequenceNumber: Long = 0L
) : Id {
    fun toTextString(): String = narrativeText.reduceOrNull { acc, s -> acc.plus(s) } ?: ""
    fun toDisplayString(): String = displayText.reduceOrNull { acc, s -> acc.plus(s) } ?: ""
}