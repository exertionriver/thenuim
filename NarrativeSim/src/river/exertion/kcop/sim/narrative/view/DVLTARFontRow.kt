package river.exertion.kcop.sim.narrative.view

import kotlinx.serialization.Serializable
import river.exertion.kcop.asset.view.FontSize

@Serializable
class DVLTARFontRow(
    var fontSize : String? = FontSize.SMALL.fontTag(),
    var allowRows : String? = 0.toString()
) {

    fun fontSize() = FontSize.byTag(fontSize)
    fun allowRows() = allowRows?.toIntOrNull() ?: 0

}
