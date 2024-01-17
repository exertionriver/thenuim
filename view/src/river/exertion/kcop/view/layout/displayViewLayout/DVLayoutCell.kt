package river.exertion.kcop.view.layout.displayViewLayout

import kotlinx.serialization.Serializable

@Serializable(with = DVLayoutCellSerializer::class)
sealed class DVLayoutCell {
        open var tag: String? = null
        abstract var cellType : DVLCellTypes
        abstract var width : String?
        abstract var height : String?

        fun dvpType() = DVPaneType.byTags(width, height)

        enum class DVLCellTypes {
                TABLE, ROW, PANE
        }
}


