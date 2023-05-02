package river.exertion.kcop.sim.narrative

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import river.exertion.kcop.view.layout.ViewType

@Serializable
class DVTextPane : DVPane() {

    override var idx : String? = null
    override val width : String? = null
    override val height : String? = null
    override val refineX : String? = null
    override val refineY : String? = null

    @Transient
    override var paneType: String? = DVPaneTypes.TEXT.tag()

    @Transient
    var paneText : String? = null

    @Transient
    @Contextual
    var textLabelStyle : LabelStyle? = null

    //pixels padding the top of pane
    @Transient
    var adjacencyTopPadOffset : Int? = 0

    //rows allowed after bottom of pane
    @Transient
    var adjacencyAllowedRows : Int? = 0

    override fun layoutPane(screenWidth : Float, screenHeight : Float, randomColorImage : Image, randomColorLabelStyle : LabelStyle, paneLabel : String?) : Stack {
        return super.layoutPane(screenWidth, screenHeight, randomColorImage, randomColorLabelStyle,  DVPaneTypes.TEXT.layoutTag())
    }

    override fun contentPane(screenWidth : Float, screenHeight : Float) : Stack {
        val textLabel = Label(paneText, textLabelStyle)
        textLabel.setAlignment(Align.topLeft)

        val textTable = Table().padLeft(ViewType.padWidth(screenWidth)).padRight(ViewType.padWidth(screenWidth))
            .padBottom(ViewType.padHeight(screenHeight) - adjacencyTopPadOffset!!)
            .padTop(ViewType.padHeight(screenHeight) + adjacencyTopPadOffset!!)
        textTable.top()
        textTable.add(textLabel).size(
            dvpType().width(screenWidth) - 2 * ViewType.padWidth(screenWidth),
            dvpType().height(screenHeight) - 2 * ViewType.padHeight(screenHeight)
        ).grow()

        val innerTableFg = Table()
        innerTableFg.top()
        innerTableFg.add(textTable)
            .size(dvpType().width(screenWidth) + refineX(), dvpType().height(screenHeight) + refineY())
            .grow()

        return Stack().apply {
            this.add(innerTableFg)
        }
    }
}