package river.exertion.kcop.view.layout.displayViewLayout

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import kotlinx.serialization.Serializable

@Serializable(with = DVPaneSerializer::class)
sealed class DVPane : DVLayoutCell() {

    override var tag : String? = null
    override var cellType: DVLCellTypes = DVLCellTypes.PANE
    abstract var refineX : String?
    abstract var refineY : String?
    abstract var padLeft : String?
    abstract var padRight : String?
    abstract var align : String?

    abstract var paneType: String?

    @Transient
    var alphaMask : Float = 0f

    fun refineX() = refineX?.toInt() ?: 0
    fun refineY() = refineY?.toInt() ?: 0

    open fun layoutPane(screenWidth : Float, screenHeight : Float, randomColorImage : Image, randomColorLabelStyle : LabelStyle, paneLabel : String? = "") : Stack {
        val label = (tag?.substring(0, 2) ?: "") + paneLabel

        val innerTableBg = Table()
        innerTableBg.add(randomColorImage).apply {
            if (this@DVPane.width != null) this.width(dvpType().width(screenWidth) + refineX())
            if (this@DVPane.height != null) this.height(dvpType().height(screenHeight) + refineY())
            this.grow()
        }

        innerTableBg.debug = true

        val innerTableFg = Table()
        val innerLabel = Label(label, randomColorLabelStyle)
        innerLabel.setAlignment(Align.center)
        innerTableFg.add(innerLabel).apply {
            if (this@DVPane.width != null) this.width(dvpType().width(screenWidth) + refineX())
            if (this@DVPane.height != null) this.height(dvpType().height(screenHeight) + refineY())
            this.grow()
        }
        innerTableFg.debug = true

        return Stack().apply {
            this.add(innerTableBg)
            this.add(innerTableFg)
        }
    }

    abstract fun contentPane(screenWidth : Float, screenHeight : Float) : Stack

    fun alphaPane(screenWidth : Float, screenHeight : Float, alphaImage : Image) : Stack {
        val innerTableFg = Table()
        innerTableFg.add(alphaImage).apply {
            if (this@DVPane.width != null) this.width(dvpType().width(screenWidth) + refineX())
            if (this@DVPane.height != null) this.height(dvpType().height(screenHeight) + refineY())
            this.grow()
        }

        return Stack().apply {
            this.add(innerTableFg)
        }
    }

    fun emptyPane(screenWidth : Float, screenHeight : Float) : Table {
        return Table().apply {
            this.add(Table()).apply {
                if (this@DVPane.width != null) this.width(dvpType().width(screenWidth) + refineX())
                if (this@DVPane.height != null) this.height(dvpType().height(screenHeight) + refineY())
                this.grow()
            }
        }
    }

    enum class DVPaneTypes {
        IMAGE { override fun tag() = "image"; override fun layoutTag() = ":I" },
        TEXT { override fun tag() = "text"; override fun layoutTag() = ":T" }
        ;
        abstract fun tag() : String
        abstract fun layoutTag() : String
        fun byTag(tag : String) = entries.firstOrNull { it.tag() == tag } ?: IMAGE
    }
}


