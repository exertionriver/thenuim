package river.exertion.kcop.sim.narrative.view

import kotlinx.serialization.Serializable
import river.exertion.kcop.asset.view.FontSize

@Serializable
class DVLTATPFontPads(
    var fontSize : String? = FontSize.SMALL.fontTag(),
    var yOffset : String? = 0.toString()
) {

    fun fontSize() = FontSize.byTag(fontSize)
    fun yOffset() = yOffset?.toIntOrNull() ?: 0

}