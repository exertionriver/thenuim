package river.exertion.kcop.simulation.view.displayViewLayouts

import kotlinx.serialization.Serializable
import river.exertion.kcop.assets.FontSize

@Serializable
class DVLTATPFontPads(
    var fontSize : String? = FontSize.SMALL.fontTag(),
    var yOffset : String? = 0.toString()
) {

    fun fontSize() = FontSize.byTag(fontSize)
    fun yOffset() = yOffset?.toIntOrNull() ?: 0

}