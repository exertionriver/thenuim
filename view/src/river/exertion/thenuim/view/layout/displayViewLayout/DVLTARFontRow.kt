package river.exertion.thenuim.view.layout.displayViewLayout

import kotlinx.serialization.Serializable
import river.exertion.thenuim.view.KcopFont

@Serializable
class DVLTARFontRow(
    var fontSize : String? = KcopFont.SMALL.fontTag(),
    var allowRows : String? = 0.toString()
) {

    fun fontSize() = KcopFont.byTag(fontSize)
    fun allowRows() = allowRows?.toIntOrNull() ?: 0

}
