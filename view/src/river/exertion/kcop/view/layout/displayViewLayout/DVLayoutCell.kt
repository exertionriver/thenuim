package river.exertion.kcop.view.layout.displayViewLayout

import kotlinx.serialization.Serializable

@Serializable(with = DVLayoutCellSerializer::class)
sealed class DVLayoutCell {
        open var tag: String? = null
        abstract var cellType : DVLCellTypes

        enum class DVLCellTypes {
                TABLE, ROW, PANE
        }
}


