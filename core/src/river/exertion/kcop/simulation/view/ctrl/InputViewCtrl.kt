package river.exertion.kcop.simulation.view.ctrl

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.simulation.view.ViewType
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.InputViewMessage

class InputViewCtrl(screenWidth: Float = 50f, screenHeight: Float = 50f) : Telegraph, ViewCtrl(ViewType.INPUT, screenWidth, screenHeight) {

    init {
        MessageChannel.INPUT_VIEW_BRIDGE.enableReceive(this)
    }

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
        currentImage = keyPressImage
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

    fun textTable(bitmapFont: BitmapFont) : Table {

        val innerTable = Table()

        innerTable.add(Label(keyText(), Label.LabelStyle(bitmapFont, backgroundColor.label().color()))).expandY()

        innerTable.row()

        if (isTouchEvent()) innerTable.add(Label(touchText(), Label.LabelStyle(bitmapFont, backgroundColor.label().color())))

//        innerTable.debug()

        return innerTable
    }

    override fun build(bitmapFont: BitmapFont, batch: Batch) {

        if ( (currentImage != null) && (isTouchEvent() || isKeyEvent()) ) {
            this.add(Stack().apply {
                this.add(backgroundColorImg(batch))
                this.add(textTable(bitmapFont))
            } ).size(this.tableWidth(), this.tableHeight())
        } else {
            this.add(backgroundColorImg(batch)).size(this.tableWidth(), this.tableHeight())
        }
        this.clip()
    }

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            if (MessageChannel.INPUT_VIEW_BRIDGE.isType(msg.message) ) {
                val inputMessage : InputViewMessage = MessageChannel.INPUT_VIEW_BRIDGE.receiveMessage(msg.extraInfo)

                if (inputMessage.event.isReleaseEvent()) {
                    releaseEvent()
                } else {
                    if (inputMessage.event.isKeyEvent()) {
                        keyEvent(inputMessage.getKeyStr())
                    }
                    else if (inputMessage.event.isTouchEvent()) {
                        touchEvent(inputMessage.getScreenX(), inputMessage.getScreenY(), inputMessage.getButton())
                    }
                }

                if (isInitialized) recreate()
                return true
            }
        }
        return false
    }
}