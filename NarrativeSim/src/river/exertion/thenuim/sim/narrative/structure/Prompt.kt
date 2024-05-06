package river.exertion.thenuim.sim.narrative.structure

import kotlinx.serialization.Serializable

@Serializable
data class Prompt(
    val promptText: String = "",
    val promptKey: Char,
    val promptNextId: String? = null,
    val promptRandomId: List<String>? = null
)