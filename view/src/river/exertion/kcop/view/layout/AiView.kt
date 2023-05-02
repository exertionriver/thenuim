package river.exertion.kcop.view.layout

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import ktx.actors.onClick
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.view.KcopSkin.Companion.KcopSkinBridge
import river.exertion.kcop.view.SdcHandler.Companion.SDCBridge
import river.exertion.kcop.view.messaging.AiHintMessage
import river.exertion.kcop.view.messaging.AiHintMessage.Companion.AiHintBridge
import river.exertion.kcop.view.messaging.DisplayModeMessage.Companion.DisplayModeBridge
import river.exertion.kcop.view.messaging.LogViewMessage
import river.exertion.kcop.view.messaging.LogViewMessage.Companion.LogViewBridge
import river.exertion.kcop.view.messaging.TextViewMessage
import river.exertion.kcop.view.messaging.TextViewMessage.Companion.TextViewBridge

class AiView(screenWidth: Float = 50f, screenHeight: Float = 50f) : Telegraph, ViewBase(ViewType.AI, screenWidth, screenHeight) {

    init {
        MessageChannelHandler.enableReceive(AiHintBridge, this)
        MessageChannelHandler.enableReceive(DisplayModeBridge, this)

        MessageChannelHandler.enableReceive(SDCBridge,this)
        MessageChannelHandler.enableReceive(KcopSkinBridge, this)
    }

    var hintTextEntries : MutableMap<String, String> = mutableMapOf()

    fun hintText() = hintTextEntries.values.reduceOrNull { acc, s -> acc + "\n$s"} ?: ""

    var isChecked = false

    fun clickButton() : Button {

        val innerButton = Button(skin())

//        kcopSkin.addOnClick(innerButton)

        //override from ctrl
        innerButton.isChecked = this@AiView.isChecked

        innerButton.onClick {
            this@AiView.isChecked = !this@AiView.isChecked
            MessageChannelHandler.send(LogViewBridge, LogViewMessage(LogViewMessage.LogViewMessageType.LogEntry, "AI character set to: ${if (this.isChecked) "On" else "Off"}" ))

            if (isChecked) {
                MessageChannelHandler.send(TextViewBridge, TextViewMessage(TextViewMessage.TextViewMessageType.HintText, hintText()))
            } else {
                MessageChannelHandler.send(TextViewBridge, TextViewMessage(TextViewMessage.TextViewMessageType.HintText))
            }
        }

        return innerButton
    }

    override fun buildCtrl() {
        this.add(Stack().apply {
            this.add(backgroundColorImg())
            this.add(Table().apply {
                this.add(clickButton()).align(Align.center).size(this@AiView.tableWidth() - 5, this@AiView.tableHeight() - 5)
            })
        } ).size(this.tableWidth(), this.tableHeight())

        this.clip()
    }

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                (MessageChannelHandler.isType(SDCBridge, msg.message) ) -> {
                    super.sdcHandler = MessageChannelHandler.receiveMessage(SDCBridge, msg.extraInfo)
                    return true
                }
                (MessageChannelHandler.isType(KcopSkinBridge, msg.message) ) -> {
                    super.kcopSkin = MessageChannelHandler.receiveMessage(KcopSkinBridge, msg.extraInfo)
                    return true
                }
                (MessageChannelHandler.isType(DisplayModeBridge, msg.message) ) -> {
                    this.currentLayoutMode = MessageChannelHandler.receiveMessage(DisplayModeBridge, msg.extraInfo)
                    build()
                    return true
                }
                (MessageChannelHandler.isType(AiHintBridge, msg.message) ) -> {
                    val aiHintMessage: AiHintMessage = MessageChannelHandler.receiveMessage(AiHintBridge, msg.extraInfo)

                    when (aiHintMessage.aiHintMessageType) {
                        AiHintMessage.AiHintMessageType.ClearHints -> hintTextEntries.clear()
                        AiHintMessage.AiHintMessageType.AddHint -> {
                            if (aiHintMessage.aiHintEventId != null && aiHintMessage.aiHintEventReport != null)
                                hintTextEntries[aiHintMessage.aiHintEventId] = aiHintMessage.aiHintEventReport
                        }
                    }

                    if (isChecked) {
                        MessageChannelHandler.send(TextViewBridge, TextViewMessage(TextViewMessage.TextViewMessageType.HintText, hintText()))
                    } else {
                        MessageChannelHandler.send(TextViewBridge, TextViewMessage(TextViewMessage.TextViewMessageType.HintText))
                    }

                    return true
                }
            }
        }
        return false
    }
}