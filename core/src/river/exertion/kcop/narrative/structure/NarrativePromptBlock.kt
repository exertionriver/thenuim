package river.exertion.kcop.narrative.structure

import kotlinx.serialization.Serializable

@Serializable
data class NarrativePromptBlock(
    val narrativeBlockId : String = "",
    val prompts : MutableList<NarrativePrompt> = mutableListOf()
)