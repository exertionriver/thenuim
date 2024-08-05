package river.exertion.thenuim.view.layout.displayViewLayout

import kotlinx.serialization.Serializable
import river.exertion.thenuim.view.TnmFont

@Serializable
class DVLTATPFontPads(
    var fontSize : String? = TnmFont.SMALL.fontTag(),
    var yOffset : String? = 0.toString()
) {

    fun fontSize() = TnmFont.byTag(fontSize)
    fun yOffset() = yOffset?.toIntOrNull() ?: 0

}