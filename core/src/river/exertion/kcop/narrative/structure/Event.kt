package river.exertion.kcop.narrative.structure

import kotlinx.serialization.Serializable
import river.exertion.kcop.Id

@Serializable
data class Event(
    override var id: String = "",
    val immersionTime: String = "",
    val event: String = "",
    val param: String = ""
) : Id()