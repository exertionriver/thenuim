package river.exertion.kcop.sim.narrative

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import kotlinx.serialization.Serializable

@Serializable(with = DVPaneSerializer::class)
sealed class DVPane : DVLayoutCell() {

    override var idx : String? = null
    override var cellType: DVLCellTypes = DVLCellTypes.PANE
    abstract val width : String?
    abstract val height : String?
    abstract val refineX : String?
    abstract val refineY : String?

    abstract var paneType: String?

    @Transient
    var alphaMask : Float = 0f

    fun idx() = idx?.toIntOrNull() ?: throw Exception("non-integer idx for DVPane!")

    fun refineX() = refineX?.toInt() ?: 0
    fun refineY() = refineY?.toInt() ?: 0

    fun dvpType() = DVPaneType.byTags(width, height)

    open fun layoutPane(screenWidth : Float, screenHeight : Float, randomColorImage : Image, randomColorLabelStyle : LabelStyle, paneLabel : String? = "") : Stack {
        val label = idx.toString() + paneLabel

        val innerTableBg = Table()
        innerTableBg.add(randomColorImage)
            .size(dvpType().width(screenWidth) + refineX(), dvpType().height(screenHeight) + refineY())
            .grow()
        innerTableBg.debug = true

        val innerTableFg = Table()
        val innerLabel = Label(label, randomColorLabelStyle)
        innerLabel.setAlignment(Align.center)
        innerTableFg.add(innerLabel)
            .size(dvpType().width(screenWidth) + refineX(), dvpType().height(screenHeight) + refineY())
            .grow()
        innerTableFg.debug = true

        return Stack().apply {
            this.add(innerTableBg)
            this.add(innerTableFg)
        }
    }

    abstract fun contentPane(screenWidth : Float, screenHeight : Float) : Stack

    fun alphaPane(screenWidth : Float, screenHeight : Float, alphaImage : Image) : Stack {
        val innerTableFg = Table()
        innerTableFg.add(alphaImage)
            .size(dvpType().width(screenWidth) + refineX(), dvpType().height(screenHeight) + refineY())
            .grow()

        return Stack().apply {
            this.add(innerTableFg)
        }
    }

    fun emptyPane(screenWidth : Float, screenHeight : Float) : Table {
        return Table().apply {
            this.add(Table())
                .size(dvpType().width(screenWidth) + refineX(), dvpType().height(screenHeight) + refineY())
                .grow()
        }
    }

    enum class DVPaneTypes {
        IMAGE { override fun tag() = "image"; override fun layoutTag() = ":I" },
        TEXT { override fun tag() = "text"; override fun layoutTag() = ":T" }
        ;
        abstract fun tag() : String
        abstract fun layoutTag() : String
        fun byTag(tag : String) = values().firstOrNull { it.tag() == tag } ?: IMAGE
    }
}


