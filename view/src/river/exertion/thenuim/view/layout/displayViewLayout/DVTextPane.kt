package river.exertion.thenuim.view.layout.displayViewLayout

import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import river.exertion.thenuim.asset.view.ColorPalette
import river.exertion.thenuim.view.KcopSkin
import river.exertion.thenuim.view.layout.ViewType
import river.exertion.thenuim.view.layout.displayViewLayout.asset.DVAlign

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
    var paneTextEdit : String? = null

    @Transient
    var paneTextEditRegEx : String? = null

    @Transient
    var colorOverride : ColorPalette? = null

    @Transient
    var textLabelStyle : LabelStyle? = null

    @Transient
    var textFieldStyle : TextField.TextFieldStyle? = null

    //pixels padding the top of pane
    @Transient
    var adjacencyTopPadOffset : Int? = null

    //rows allowed after bottom of pane
    @Transient
    var adjacencyAllowedRows : Int? = null

    @Transient
    var mode : DVTextPaneMode? = null

    @Transient
    var writeCallback : ((String) -> Unit)? = null

    @Transient
    var paneTextField = TextField(paneText, KcopSkin.textFieldStyle(
        DVLayoutHandler.currentFontSize,
        DVLayoutHandler.currentFontColor
    ))

    @Transient
    var paneTextLabel = Label(paneText, KcopSkin.labelStyle(
        DVLayoutHandler.currentFontSize,
        DVLayoutHandler.currentFontColor
    ))

    fun callCallback(writeString : String) = writeCallback?.let { it(writeString) }

    override fun layoutPane(screenWidth : Float, screenHeight : Float, randomColorImage : Image, randomColorLabelStyle : LabelStyle, paneLabel : String?) : Stack {
        return super.layoutPane(screenWidth, screenHeight, randomColorImage, randomColorLabelStyle,  DVPaneTypes.TEXT.layoutTag())
    }

    var editRegex = "[a-zA-Z0-9]+"

    val tfListener = TextFieldListener { textField, c ->
    if (c == '\n') {
        if (editRegex.toRegex().matches(textField.text)) {
            this@DVTextPane.paneText = paneTextEdit
            this@DVTextPane.mode = DVTextPaneMode.READ
            paneTextEdit?.let { callCallback(it) }
        } else {
            this@DVTextPane.paneTextEdit = paneText
        }
    }
    else {
        this@DVTextPane.paneTextEdit = textField.text
        if (editRegex.toRegex().matches(textField.text)) {
            this.textFieldStyle = KcopSkin.textFieldStyle(DVLayoutHandler.currentFontSize, ColorPalette.of("green"))
        } else {
            this.textFieldStyle = KcopSkin.textFieldStyle(DVLayoutHandler.currentFontSize, ColorPalette.of("red"))
        }
        paneTextField.style = this.textFieldStyle
    }}

    override fun contentPane(screenWidth : Float, screenHeight : Float) : Stack {
        if (colorOverride != null) {
            this.textLabelStyle = KcopSkin.labelStyle(DVLayoutHandler.currentFontSize, colorOverride)
            this.textFieldStyle = KcopSkin.textFieldStyle(DVLayoutHandler.currentFontSize, colorOverride)
        }

        paneTextField.setTextFieldListener(this@DVTextPane.tfListener)

        paneTextField.text = paneText
        paneTextField.alignment = DVAlign.byTag(this@DVTextPane.align).align()
//        paneTextField.debug = true

        paneTextLabel = Label(paneText, textLabelStyle)
        paneTextLabel.setAlignment(DVAlign.byTag(this@DVTextPane.align).align())

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
        if (mode == DVTextPaneMode.WRITE) {
            textTable.add(paneTextField).apply {
                if (this@DVTextPane.width != null) this.width(dvpType().width(screenWidth) - proportionalPadLeft - proportionalPadRight)
                if (this@DVTextPane.height != null) this.height(dvpType().height(screenHeight) - 2 * ViewType.padHeight(screenHeight))
            }
        } else {
            textTable.add(paneTextLabel).apply {
                if (this@DVTextPane.width != null) this.width(dvpType().width(screenWidth) - proportionalPadLeft - proportionalPadRight)
                if (this@DVTextPane.height != null) this.height(dvpType().height(screenHeight) - 2 * ViewType.padHeight(screenHeight))
            }

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

    //remember to minimize InputProcessor handling during WRITE mode
    enum class DVTextPaneMode {
        READ, WRITE
    }
}