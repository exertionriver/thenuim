package river.exertion.kcop.simulation.view

import com.badlogic.gdx.ai.fsm.DefaultStateMachine
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.narrative.structure.Narrative
import river.exertion.kcop.simulation.SimulationState
import river.exertion.kcop.system.MessageChannel
import river.exertion.kcop.system.view.*

class ViewLayout(var width : Float, var height : Float) : Telegraph {

    var displayViewCtrl = ViewCtrl(ViewType.DISPLAY, width, height)
    var textViewCtrl = TextViewCtrl(width, height)
    var logViewCtrl = LogViewCtrl(width, height)
    var statusViewCtrl = ViewCtrl(ViewType.STATUS, width, height)
    var menuViewCtrl = ViewCtrl(ViewType.MENU, width, height)
    var inputsViewCtrl = InputViewCtrl(width, height)
    var aiViewCtrl = ViewCtrl(ViewType.AI, width, height)
    var pauseViewCtrl = PauseViewCtrl(width, height)

    var textNarrativesIdx = 0
    var textNarratives : List<Narrative>? = null

    fun isTextNarratives() = !textNarratives.isNullOrEmpty()

    init {
        MessageChannel.LAYOUT_BRIDGE.enableReceive(this)
        MessageChannel.TEXT_VIEW_BRIDGE.enableReceive(this)
        MessageChannel.LOG_VIEW_BRIDGE.enableReceive(this)
        MessageChannel.INPUT_VIEW_BRIDGE.enableReceive(this)
    }

    val stateMachine = DefaultStateMachine(this, SimulationState.RUNNING)

    private fun createViewCtrl(layoutViewCtrl : ViewCtrl, batch : Batch, bitmapFont : BitmapFont) : Table {
        layoutViewCtrl.initCreate(bitmapFont, batch)
        return layoutViewCtrl
    }

    fun createDisplayViewCtrl(batch : Batch, bitmapFont : BitmapFont) = createViewCtrl(displayViewCtrl, batch, bitmapFont)
    fun createTextViewCtrl(batch : Batch, bitmapFont : BitmapFont, textNarratives : List<Narrative>, vScrollKnobImage : Texture) : TextViewCtrl {
        this.textNarratives = textNarratives

        textViewCtrl.vScrollKnobTexture = vScrollKnobImage

        textViewCtrl.currentText = textNarratives[textNarrativesIdx].currentText()
        textViewCtrl.currentPrompts = textNarratives[textNarrativesIdx].currentPrompts()
        textViewCtrl.initCreate(bitmapFont, batch)

        return textViewCtrl
    }
    fun createLogViewCtrl(batch : Batch, bitmapFont : BitmapFont, vScrollImage : Texture, vScrollKnobImage : Texture) : LogViewCtrl {
        logViewCtrl.vScrollTexture = vScrollImage
        logViewCtrl.vScrollKnobTexture = vScrollKnobImage

        logViewCtrl.initCreate(bitmapFont, batch)

        return logViewCtrl
    }
    fun createMenuViewCtrl(batch : Batch, bitmapFont : BitmapFont) = createViewCtrl(statusViewCtrl, batch, bitmapFont)
    fun createPromptsViewCtrl(batch : Batch, bitmapFont : BitmapFont) = createViewCtrl(menuViewCtrl, batch, bitmapFont)
    fun createInputsViewCtrl(batch : Batch, bitmapFont : BitmapFont, clickImage : Texture, keyPressImage : Texture, keyUpImage : Texture) : InputViewCtrl {
        inputsViewCtrl.clickImage = clickImage
        inputsViewCtrl.keyPressImage = keyPressImage
        inputsViewCtrl.keyUpImage = keyUpImage

        inputsViewCtrl.initCreate(bitmapFont, batch)

        return inputsViewCtrl
    }
    fun createAiViewCtrl(batch : Batch, bitmapFont : BitmapFont) = createViewCtrl(aiViewCtrl, batch, bitmapFont)
    fun createPauseViewCtrl(batch : Batch, bitmapFont : BitmapFont, upImage : Texture, downImage : Texture, checkedImage : Texture) : PauseViewCtrl {
        pauseViewCtrl.pauseUpImage = upImage
        pauseViewCtrl.pauseDownImage = downImage
        pauseViewCtrl.pauseCheckedImage = checkedImage

        pauseViewCtrl.initCreate(bitmapFont, batch)

        return pauseViewCtrl
    }

    fun prevNarrativeIdx() {
        if (isTextNarratives()) {
            textNarrativesIdx = (textNarrativesIdx - 1).coerceAtLeast(0)
            textViewCtrl.currentText = textNarratives!![textNarrativesIdx].currentText()
            textViewCtrl.currentPrompts = textNarratives!![textNarrativesIdx].currentPrompts()
            textViewCtrl.recreate()
        }
    }

    fun nextNarrativeIdx() {
        if (isTextNarratives()) {
            textNarrativesIdx = (textNarrativesIdx + 1).coerceAtMost(textNarratives!!.size - 1)
            textViewCtrl.currentText = textNarratives!![textNarrativesIdx].currentText()
            textViewCtrl.currentPrompts = textNarratives!![textNarrativesIdx].currentPrompts()
            textViewCtrl.recreate()
        }
    }

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                (MessageChannel.INPUT_VIEW_BRIDGE.isType(msg.message) ) -> {
                    val inputMessage : InputViewMessage = MessageChannel.INPUT_VIEW_BRIDGE.receiveMessage(msg.extraInfo)

                    if (inputMessage.event.isReleaseEvent()) {
                        inputsViewCtrl.releaseEvent()
                    } else {
                        if (inputMessage.event.isKeyEvent()) {
                            inputsViewCtrl.keyEvent(inputMessage.getKeyStr())
                        }
                        else if (inputMessage.event.isTouchEvent()) {
                            inputsViewCtrl.touchEvent(inputMessage.getScreenX(), inputMessage.getScreenY(), inputMessage.getButton())
                        }
                    }
                    if (inputsViewCtrl.isInitialized) inputsViewCtrl.recreate()
                }
                (MessageChannel.LOG_VIEW_BRIDGE.isType(msg.message) ) -> {
                    val logMessage : LogViewMessage = MessageChannel.LOG_VIEW_BRIDGE.receiveMessage(msg.extraInfo)

                    when (logMessage.messageType) {
                        LogViewMessageType.LogEntry -> {
                            logViewCtrl.addLog(logMessage.message)
                            if (logViewCtrl.isInitialized) logViewCtrl.recreate()
                        }
                        LogViewMessageType.ImmersionTime -> {
                            logViewCtrl.updateImmersionTime(logMessage.message)
                            if (logViewCtrl.isInitialized) logViewCtrl.rebuildTextTimeReadout()
                        }
                        LogViewMessageType.LocalTime -> {
                            logViewCtrl.updateLocalTime(logMessage.message)
                            if (logViewCtrl.isInitialized) logViewCtrl.rebuildTextTimeReadout()
                        }
                    }
                }
                (MessageChannel.LAYOUT_BRIDGE.isType(msg.message) ) -> {
                    val viewMessage: ViewMessage = MessageChannel.LAYOUT_BRIDGE.receiveMessage(msg.extraInfo)

                    if ((viewMessage.targetView == ViewType.PAUSE) && (viewMessage.messageContent == ViewMessage.TogglePause)) {
                        this.stateMachine.update()
                    }
                }
                (MessageChannel.TEXT_VIEW_BRIDGE.isType(msg.message) ) -> {
                    val viewMessage: ViewMessage = MessageChannel.TEXT_VIEW_BRIDGE.receiveMessage(msg.extraInfo)

                    if ( (viewMessage.targetView == ViewType.TEXT) && isTextNarratives() ) {
                        textNarratives!![textNarrativesIdx].next(viewMessage.messageContent)
                        textViewCtrl.currentText = textNarratives!![textNarrativesIdx].currentText()
                        textViewCtrl.currentPrompts = textNarratives!![textNarrativesIdx].currentPrompts()
                        if (textViewCtrl.isInitialized) textViewCtrl.recreate()
                    }
                }
            }
        }
        return true
    }

    fun dispose() {
        displayViewCtrl.dispose()
        textViewCtrl.dispose()
        logViewCtrl.dispose()
        statusViewCtrl.dispose()
        menuViewCtrl.dispose()
        inputsViewCtrl.dispose()
        aiViewCtrl.dispose()
        pauseViewCtrl.dispose()
    }
}