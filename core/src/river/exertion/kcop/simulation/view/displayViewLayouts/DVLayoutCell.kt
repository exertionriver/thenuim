package river.exertion.kcop.simulation.view.displayViewLayouts

import kotlinx.serialization.Serializable

@Serializable(with = DVLayoutCellSerializer::class)
sealed class DVLayoutCell {
        open var idx: String? = null
        abstract var cellType : DVLCellTypes

        enum class DVLCellTypes {
                TABLE, ROW, PANE
        }
}


