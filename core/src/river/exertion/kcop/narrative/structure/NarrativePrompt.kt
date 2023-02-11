package river.exertion.kcop.narrative.structure

import kotlinx.serialization.Serializable
import river.exertion.kcop.Id

@Serializable
data class NarrativePrompt(
    override var id: String = "",
    val promptText: String = "",
    val promptKey: Char,
    val promptNextId: String = ""
) : Id()