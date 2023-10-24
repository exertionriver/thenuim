package river.exertion.kcop.view.layout.displayViewLayout

import kotlinx.serialization.Serializable

@Serializable
class DVRow(
    override var cellType: DVLCellTypes = DVLCellTypes.ROW
    ) : DVLayoutCell()


