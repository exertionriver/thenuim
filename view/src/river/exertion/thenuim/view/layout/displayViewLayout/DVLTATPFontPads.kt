package river.exertion.thenuim.view.layout.displayViewLayout

import kotlinx.serialization.Serializable
import river.exertion.thenuim.view.KcopFont

@Serializable
class DVLTATPFontPads(
    var fontSize : String? = KcopFont.SMALL.fontTag(),
    var yOffset : String? = 0.toString()
) {

    fun fontSize() = KcopFont.byTag(fontSize)
    fun yOffset() = yOffset?.toIntOrNull() ?: 0

}