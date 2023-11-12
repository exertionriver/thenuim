package river.exertion.kcop.view.layout.displayViewLayout

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import river.exertion.kcop.view.layout.ViewType
import river.exertion.kcop.view.layout.displayViewLayout.asset.DVAlign

@Serializable
class DVTextPane : DVPane() {

    override var tag : String? = null
    override var width : String? = null
    override var height : String? = null
    override var refineX : String? = null
    override var refineY : String? = null
    override var padLeft : String? = null
    override var padRight : String? = null
    override var align : String? = null

    @Transient
    override var paneType: String? = DVPaneTypes.TEXT.tag()

    @Transient
    var paneText : String? = null

    @Transient
    @Contextual
    var textLabelStyle : LabelStyle? = null

    //pixels padding the top of pane
    @Transient
    var adjacencyTopPadOffset : Int? = null

    //rows allowed after bottom of pane
    @Transient
    var adjacencyAllowedRows : Int? = null

    override fun layoutPane(screenWidth : Float, screenHeight : Float, randomColorImage : Image, randomColorLabelStyle : LabelStyle, paneLabel : String?) : Stack {
        return super.layoutPane(screenWidth, screenHeight, randomColorImage, randomColorLabelStyle,  DVPaneTypes.TEXT.layoutTag())
    }

    override fun contentPane(screenWidth : Float, screenHeight : Float) : Stack {
        val textLabel = Label(paneText, textLabelStyle)
        textLabel.setAlignment(DVAlign.byTag(this@DVTextPane.align).align())
//        textLabel.debug = true

        val proportionalPadLeft = dvpType().width(screenWidth) * (this@DVTextPane.padLeft ?: "0").toFloat()
        val proportionalPadRight = dvpType().width(screenWidth) * (this@DVTextPane.padRight ?: "0").toFloat()

        val textTable = Table().apply {
            this.padLeft(proportionalPadLeft)
            this.padRight(proportionalPadRight)
            if (this@DVTextPane.adjacencyTopPadOffset != null) {
                this.padBottom(ViewType.padHeight(screenHeight) - adjacencyTopPadOffset!!)
                .padTop(ViewType.padHeight(screenHeight) + adjacencyTopPadOffset!!)
            }
        }
        textTable.top()
        textTable.add(textLabel).apply {
            if (this@DVTextPane.width != null) this.width(dvpType().width(screenWidth) - proportionalPadLeft - proportionalPadRight)
            if (this@DVTextPane.height != null) this.height(dvpType().height(screenHeight) - 2 * ViewType.padHeight(screenHeight))
        }

//        textTable.debug = true

        val innerTableFg = Table()
        innerTableFg.top()
        innerTableFg.add(textTable).apply {
            if (this@DVTextPane.width != null) this.width(dvpType().width(screenWidth) + refineX())
            if (this@DVTextPane.height != null) this.height(dvpType().height(screenHeight) + refineY())
            this.grow()
        }

//        innerTableFg.debug = true

        return Stack().apply {
            this.add(innerTableFg)
        }
    }
}