package river.exertion.kcop.narrative.navigation

import kotlinx.serialization.Serializable
import river.exertion.kcop.narrative.NarrativeText
import river.exertion.kcop.narrative.sequence.Sequence
import river.exertion.kcop.system.Id

@Serializable
data class NarrativeNavigationBlock(
    override val narrativeText: String = "",
    override val sequenceNumber: Long = 0L,
    val prompts : MutableList<NarrativeNavigationPrompt> = mutableListOf(),
    override val id: String = ""
) : Id, Sequence, NarrativeText