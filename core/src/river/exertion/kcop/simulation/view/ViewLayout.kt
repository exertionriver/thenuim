package river.exertion.kcop.simulation.view

import com.badlogic.gdx.Input
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Table
import river.exertion.kcop.narrative.structure.Narrative
import river.exertion.kcop.system.MessageChannel
import river.exertion.kcop.system.component.ImmersionTimerComponent
import river.exertion.kcop.system.component.NarrativeComponent
import river.exertion.kcop.system.view.*

class ViewLayout(var width : Float, var height : Float) : Telegraph {

    var displayViewCtrl = DisplayViewCtrl(width, height)
    var textViewCtrl = TextViewCtrl(width, height)
    var logViewCtrl = LogViewCtrl(width, height)
    var statusViewCtrl = ViewCtrl(ViewType.STATUS, width, height)
    var menuViewCtrl = ViewCtrl(ViewType.MENU, width, height)
    var inputsViewCtrl = InputViewCtrl(width, height)
    var aiViewCtrl = ViewCtrl(ViewType.AI, width, height)
    var pauseViewCtrl = PauseViewCtrl(width, height)

    var currentNarrativeId : String? = null
    var currentInstImmersionTimerId : String? = null
    var currentCumlImmersionTimerId : String? = null
    var currentInstBlockTimerId : String? = null
    var currentCumlBlockTimerId : String? = null

    init {
        MessageChannel.LAYOUT_BRIDGE.enableReceive(this)
        MessageChannel.DISPLAY_VIEW_BRIDGE.enableReceive(this)
        MessageChannel.TEXT_VIEW_BRIDGE.enableReceive(this)
        MessageChannel.LOG_VIEW_BRIDGE.enableReceive(this)
        MessageChannel.INPUT_VIEW_BRIDGE.enableReceive(this)
        MessageChannel.NARRATIVE_PROMPT_BRIDGE_PAUSE_GATE.enableReceive(this)
    }

    private fun createViewCtrl(layoutViewCtrl : ViewCtrl, batch : Batch, bitmapFont : BitmapFont) : Table {
        layoutViewCtrl.initCreate(bitmapFont, batch)
        return layoutViewCtrl
    }

    fun createDisplayViewCtrl(batch : Batch, bitmapFont : BitmapFont, largeImage : Texture? = null, mediumImage : Texture? = null, smallImage : Texture? = null, tinyImage : Texture? = null) : DisplayViewCtrl {
        displayViewCtrl.largeImage = largeImage
        displayViewCtrl.mediumImage = mediumImage
        displayViewCtrl.smallImage = smallImage
        displayViewCtrl.tinyImage = tinyImage

        displayViewCtrl.initCreate(bitmapFont, batch)

        return displayViewCtrl
    }

    fun createTextViewCtrl(batch : Batch, bitmapFont : BitmapFont, vScrollKnobImage : Texture) : TextViewCtrl {
        textViewCtrl.vScrollKnobTexture = vScrollKnobImage

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

    fun resetNarrative(instImmersionTimerId : String, cumlImmersionTimerId : String, instBlockTimerId : String, cumlBlockTimerId : String,
                       narrativeId : String) {
        currentInstImmersionTimerId = instImmersionTimerId
        currentCumlImmersionTimerId = cumlImmersionTimerId
        currentInstBlockTimerId = instBlockTimerId
        currentCumlBlockTimerId = cumlBlockTimerId
        currentNarrativeId = narrativeId
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
                        LogViewMessageType.InstImmersionTime -> {
                            if ( (logMessage.param != null) && (logMessage.param == this.currentInstImmersionTimerId) ) {
                                logViewCtrl.updateInstImmersionTime(logMessage.message)
                                if (logViewCtrl.isInitialized) logViewCtrl.rebuildTextTimeReadout()
                            }
                        }
                        LogViewMessageType.CumlImmersionTime -> {
                            if ( (logMessage.param != null) && (logMessage.param == this.currentCumlImmersionTimerId) ) {
                                logViewCtrl.updateCumlImmersionTime(logMessage.message)
                                if (logViewCtrl.isInitialized) logViewCtrl.rebuildTextTimeReadout()
                            }
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
                        MessageChannel.IMMERSION_TIME_BRIDGE.send(null, ImmersionTimerMessage(this.currentInstImmersionTimerId, null) )
                        MessageChannel.IMMERSION_TIME_BRIDGE.send(null, ImmersionTimerMessage(this.currentCumlImmersionTimerId, null) )
                        MessageChannel.IMMERSION_TIME_BRIDGE.send(null, ImmersionTimerMessage(this.currentInstBlockTimerId, null) )
                        MessageChannel.IMMERSION_TIME_BRIDGE.send(null, ImmersionTimerMessage(this.currentCumlBlockTimerId, null) )
                    }
                    if ( (viewMessage.targetView == ViewType.LOG) && (viewMessage.messageContent == ViewMessage.BlockInstTimer) ) {
                        this.currentInstBlockTimerId = viewMessage.param
                    }
                    if ( (viewMessage.targetView == ViewType.LOG) && (viewMessage.messageContent == ViewMessage.BlockCumlTimer) ) {
                        this.currentCumlBlockTimerId = viewMessage.param
                    }
                }
                (MessageChannel.TEXT_VIEW_BRIDGE.isType(msg.message) ) -> {
                    val textViewMessage: TextViewMessage = MessageChannel.TEXT_VIEW_BRIDGE.receiveMessage(msg.extraInfo)

                    if ( textViewMessage.param == this.currentNarrativeId ) {
                        textViewCtrl.currentText = textViewMessage.narrativeText
                        textViewCtrl.currentPrompts = textViewMessage.prompts
                        if (textViewCtrl.isInitialized) textViewCtrl.recreate()
                    }
                }
                (MessageChannel.NARRATIVE_PROMPT_BRIDGE_PAUSE_GATE.isType(msg.message) ) -> {
                    val promptMessage: ViewMessage = MessageChannel.NARRATIVE_PROMPT_BRIDGE_PAUSE_GATE.receiveMessage(msg.extraInfo)

                    if (!pauseViewCtrl.isChecked) {
                        MessageChannel.NARRATIVE_PROMPT_BRIDGE.send(null, promptMessage)
                    }
                }
                (MessageChannel.DISPLAY_VIEW_BRIDGE.isType(msg.message) ) -> {
                    val displayViewMessage: DisplayViewMessage = MessageChannel.DISPLAY_VIEW_BRIDGE.receiveMessage(msg.extraInfo)

                    when (displayViewMessage.messageType) {
                        DisplayViewMessageType.IMAGE_LARGE -> this.displayViewCtrl.largeImage = displayViewMessage.texture
                        DisplayViewMessageType.IMAGE_MEDIUM -> this.displayViewCtrl.mediumImage = displayViewMessage.texture
                        DisplayViewMessageType.IMAGE_SMALL -> this.displayViewCtrl.smallImage = displayViewMessage.texture
                        DisplayViewMessageType.IMAGE_CLEAR -> {
                            this.displayViewCtrl.largeImage = null
                            this.displayViewCtrl.mediumImage = null
                            this.displayViewCtrl.smallImage = null
                            this.displayViewCtrl.tinyImage = null
                        }
                    }

                    this.displayViewCtrl.recreate()
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