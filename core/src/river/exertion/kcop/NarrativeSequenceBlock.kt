package river.exertion.kcop

import kotlinx.serialization.Serializable

@Serializable
data class NarrativeSequenceBlock(
    override val narrativeText: String = "",
    override val sequenceId: Long = 0L,
    override val id: String = ""
) : NarrativeSequencePlugin {
}