package river.exertion.thenuim.view.layout

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.thenuim.view.TnmSkin
import river.exertion.thenuim.view.TnmFont

object InputView : ViewBase {

    override var viewType = ViewType.INPUT
    override var viewTable = Table()

    var clickImage : Texture? = null
    var keyPressImage : Texture? = null
    var keyUpImage : Texture? = null

    var currentImage : Texture? = null
    var currentKey : String? = null
    var currentButton : Int? = null
    var currentClickX : Int? = null
    var currentClickY : Int? = null

    fun releaseEvent() {
        currentImage = keyUpImage
        currentKey = null
        currentButton = null
        currentClickX = null
        currentClickY = null
    }

    fun keyEvent(keyPress : String) {
//        currentImage = keyPressImage
        currentKey = keyPress
    }

    fun isKeyEvent() = currentKey != null

    fun keyText() = currentKey

    fun touchEvent(xClickPos : Int, yClickPos : Int, button : Int) {
        currentImage = clickImage
        currentButton = button
        currentClickX = xClickPos
        currentClickY = yClickPos
    }

    fun isTouchEvent() = (currentClickX != null) && (currentClickY != null) && (currentButton != null)

    fun touchText() = "$currentButton ($currentClickX,$currentClickY)"

    fun textTable() : Table {

        val innerTable = Table()

        innerTable.add(Label(keyText(), TnmSkin.labelStyle(TnmFont.TEXT, backgroundColor().label())))
        .expandY()

        innerTable.row()

        if (isTouchEvent()) innerTable.add(Label(touchText(), TnmSkin.labelStyle(TnmFont.TEXT, backgroundColor().label())))

        if (TnmSkin.displayMode) innerTable.debug()

        return innerTable
    }

    override fun buildCtrl() {

        if ( (isTouchEvent() || isKeyEvent()) ) {
            viewTable.add(Stack().apply {
                this.add(backgroundColorImg())
                this.add(textTable())
            } ).size(this.tableWidth(), this.tableHeight())
        } else {
            viewTable.add(backgroundColorImg()).size(this.tableWidth(), this.tableHeight())
        }
        viewTable.clip()
    }

}