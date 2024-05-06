package river.exertion.thenuim.view.layout.displayViewLayout

import kotlinx.serialization.Serializable

@Serializable
data class DVTable(
    val tableTag : String,
    override var cellType: DVLCellTypes = DVLCellTypes.TABLE,
    override var width : String? = null,
    override var height : String? = null,
    val colspan : String? = null,
    val panes : MutableList<DVLayoutCell> = mutableListOf()
) : DVLayoutCell() {

    fun colspan() = colspan?.toIntOrNull() ?: 1

}