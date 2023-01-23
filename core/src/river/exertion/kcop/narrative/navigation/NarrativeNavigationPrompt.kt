package river.exertion.kcop.narrative.navigation

import kotlinx.serialization.Serializable
import river.exertion.kcop.system.Id

@Serializable
data class NarrativeNavigationPrompt(
    val promptText: String = "",
    val promptKey: Char,
    val promptSequenceNumber: Long = 0L,
    override val id: String = ""
) : Id {
}