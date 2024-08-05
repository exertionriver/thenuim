package river.exertion.thenuim.view.layout.displayViewLayout

import kotlinx.serialization.Serializable
import river.exertion.thenuim.view.TnmFont

@Serializable
class DVLTARFontRow(
    var fontSize : String? = TnmFont.SMALL.fontTag(),
    var allowRows : String? = 0.toString()
) {

    fun fontSize() = TnmFont.byTag(fontSize)
    fun allowRows() = allowRows?.toIntOrNull() ?: 0

}
