package river.exertion.kcop.simulation.view

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.system.MessageChannel
import river.exertion.kcop.system.view.InputViewMessage
import river.exertion.kcop.system.view.ViewType

class ViewLayout(var width : Float, var height : Float) : Telegraph {

    var displayViewCtrl = ViewCtrl(ViewType.DISPLAY, width, height)
    var textViewCtrl = ViewCtrl(ViewType.TEXT, width, height)
    var logViewCtrl = ViewCtrl(ViewType.LOG, width, height)
    var menuViewCtrl = ViewCtrl(ViewType.MENU, width, height)
    var promptsViewCtrl = ViewCtrl(ViewType.PROMPTS, width, height)
    var inputsViewCtrl = InputViewCtrl(width, height)
    var aiViewCtrl = ViewCtrl(ViewType.AI, width, height)
    var pauseViewCtrl = ViewCtrl(ViewType.PAUSE, width, height)

    init {
        MessageChannel.LAYOUT_BRIDGE.enableReceive(this)
        MessageChannel.INPUT_VIEW_BRIDGE.enableReceive(this)
    }

    private fun createViewCtrl(layoutViewCtrl : ViewCtrl, batch : Batch, bitmapFont : BitmapFont) : Table {
        layoutViewCtrl.initCreate(bitmapFont, batch)
        return layoutViewCtrl
    }

    fun createDisplayViewCtrl(batch : Batch, bitmapFont : BitmapFont) = createViewCtrl(displayViewCtrl, batch, bitmapFont)
    fun createTextViewCtrl(batch : Batch, bitmapFont : BitmapFont) = createViewCtrl(textViewCtrl, batch, bitmapFont)
    fun createLogViewCtrl(batch : Batch, bitmapFont : BitmapFont) = createViewCtrl(logViewCtrl, batch, bitmapFont)
    fun createMenuViewCtrl(batch : Batch, bitmapFont : BitmapFont) = createViewCtrl(menuViewCtrl, batch, bitmapFont)
    fun createPromptsViewCtrl(batch : Batch, bitmapFont : BitmapFont) = createViewCtrl(promptsViewCtrl, batch, bitmapFont)
    fun createInputsViewCtrl(batch : Batch, bitmapFont : BitmapFont, clickImage : Texture, keyPressImage : Texture, keyUpImage : Texture) : InputViewCtrl {
        inputsViewCtrl.clickImage = clickImage
        inputsViewCtrl.keyPressImage = keyPressImage
        inputsViewCtrl.keyUpImage = keyUpImage

        inputsViewCtrl.initCreate(bitmapFont, batch)

        return inputsViewCtrl
    }
    fun createAiViewCtrl(batch : Batch, bitmapFont : BitmapFont) = createViewCtrl(aiViewCtrl, batch, bitmapFont)
    fun createPauseViewCtrl(batch : Batch, bitmapFont : BitmapFont) = createViewCtrl(pauseViewCtrl, batch, bitmapFont)

    override fun handleMessage(msg: Telegram?): Boolean {
        if ( (msg != null) && (MessageChannel.INPUT_VIEW_BRIDGE.isType(msg.message) ) ) {
            val inputMessage : InputViewMessage = MessageChannel.INPUT_VIEW_BRIDGE.receiveMessage(msg.extraInfo)

            if (InputViewMessage.isReleaseEvent(inputMessage.event)) {
                inputsViewCtrl.releaseEvent()
            } else {
                if (InputViewMessage.isKeyEvent(inputMessage.event)) {
                    inputsViewCtrl.keyEvent(inputMessage.getKeyStr())
                }
                else if (InputViewMessage.isTouchEvent(inputMessage.event)) {
                    inputsViewCtrl.touchEvent(inputMessage.getScreenX(), inputMessage.getScreenY(), inputMessage.getButton())
                }
            }
            inputsViewCtrl.recreate()
        }
        return true
    }

}