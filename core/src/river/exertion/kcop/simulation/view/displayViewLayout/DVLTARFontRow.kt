package river.exertion.kcop.simulation.view.displayViewLayout

import kotlinx.serialization.Serializable
import river.exertion.kcop.assets.FontSize

@Serializable
class DVLTARFontRow(
    var fontSize : String? = FontSize.SMALL.fontTag(),
    var allowRows : String? = 0.toString()
) {

    fun fontSize() = FontSize.byTag(fontSize)
    fun allowRows() = allowRows?.toIntOrNull() ?: 0

}
