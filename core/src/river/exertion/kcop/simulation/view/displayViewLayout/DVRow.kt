package river.exertion.kcop.simulation.view.displayViewLayout

import kotlinx.serialization.Serializable

@Serializable
class DVRow(
    override var cellType: DVLCellTypes = DVLCellTypes.ROW
    ) : DVLayoutCell()


