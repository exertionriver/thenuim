package river.exertion.kcop.narrative.structure

import kotlinx.serialization.Serializable

@Serializable
data class PromptBlock(
    val narrativeBlockId : String = "",
    val prompts : MutableList<Prompt> = mutableListOf()
)