package river.exertion.thenuim.view.layout.displayViewLayout

import kotlinx.serialization.Serializable

@Serializable
class DVRow(
    override var cellType: DVLCellTypes = DVLCellTypes.ROW,
    //not used
    override var width : String? = null,
    override var height : String? = null,
    ) : DVLayoutCell()


