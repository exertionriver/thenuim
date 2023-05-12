package river.exertion.kcop.view.layout

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.asset.view.FontSize
import river.exertion.kcop.asset.view.KcopSkin
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.view.ViewPackage.InputViewBridge
import river.exertion.kcop.view.messaging.InputViewMessage

class InputView : Telegraph, ViewBase(ViewType.INPUT) {

    init {
        MessageChannelHandler.enableReceive(InputViewBridge, this)
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

        innerTable.add(Label(keyText(), KcopSkin.labelStyle(FontSize.TEXT, backgroundColor)))
        .expandY()

        innerTable.row()

        if (isTouchEvent()) innerTable.add(Label(touchText(), KcopSkin.labelStyle(FontSize.TEXT, backgroundColor)))

//        innerTable.debug()

        return innerTable
    }

    override fun buildCtrl() {

        if ( (isTouchEvent() || isKeyEvent()) ) {
            this.add(Stack().apply {
                this.add(backgroundColorImg())
                this.add(textTable())
            } ).size(this.tableWidth(), this.tableHeight())
        } else {
            this.add(backgroundColorImg()).size(this.tableWidth(), this.tableHeight())
        }
        this.clip()
    }

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                (MessageChannelHandler.isType(InputViewBridge, msg.message) ) -> {
                    val inputMessage : InputViewMessage = MessageChannelHandler.receiveMessage(InputViewBridge, msg.extraInfo)

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

                    build()
                    return true
                }

            }
        }
        return false
    }
}