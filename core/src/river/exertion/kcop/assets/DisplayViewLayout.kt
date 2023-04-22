package river.exertion.kcop.assets

import kotlinx.serialization.Serializable
import river.exertion.kcop.Id

@Serializable
data class DisplayViewLayout(
    override var id: String = Id.randomId(),
    val name : String,
    val layout : MutableList<DisplayViewLayoutTable> = mutableListOf()
) : Id

