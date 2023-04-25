package river.exertion.kcop.simulation.view.displayViewLayouts

import kotlinx.serialization.Serializable

@Serializable
class DVRow(
    override var cellType: DVLCellTypes = DVLCellTypes.ROW
    ) : DVLayoutCell()


