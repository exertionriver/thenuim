package river.exertion.kcop.sim.narrative.view

import kotlinx.serialization.Serializable

@Serializable
class DVRow(
    override var cellType: DVLCellTypes = DVLCellTypes.ROW
    ) : DVLayoutCell()


