package river.exertion.kcop.assets

import kotlinx.serialization.Serializable

@Serializable
data class DisplayViewLayoutTable(
    val tableIdx : String,
    val panes : MutableList<DisplayViewLayoutCell> = mutableListOf()
) : DisplayViewLayoutCell()