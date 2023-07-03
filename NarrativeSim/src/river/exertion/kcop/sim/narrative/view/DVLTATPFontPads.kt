package river.exertion.kcop.sim.narrative.view

import kotlinx.serialization.Serializable
import river.exertion.kcop.view.KcopFont

@Serializable
class DVLTATPFontPads(
    var fontSize : String? = KcopFont.SMALL.fontTag(),
    var yOffset : String? = 0.toString()
) {

    fun fontSize() = KcopFont.byTag(fontSize)
    fun yOffset() = yOffset?.toIntOrNull() ?: 0

}