package river.exertion.kcop.view.layout

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import ktx.actors.onClick
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.view.KcopSkin
import river.exertion.kcop.view.ViewPackage.AiHintBridge
import river.exertion.kcop.view.ViewPackage.LogViewBridge
import river.exertion.kcop.view.ViewPackage.TextViewBridge
import river.exertion.kcop.view.messaging.AiHintMessage
import river.exertion.kcop.view.messaging.LogViewMessage
import river.exertion.kcop.view.messaging.TextViewMessage

class AiView : Telegraph, ViewBase(ViewType.AI) {

    init {
        MessageChannelHandler.enableReceive(AiHintBridge, this)
    }

    var hintTextEntries : MutableMap<String, String> = mutableMapOf()

    fun hintText() = hintTextEntries.values.reduceOrNull { acc, s -> acc + "\n$s"} ?: ""

    var isChecked = false

    fun clickButton() : Button {

        val innerButton = Button(KcopSkin.skin)

        KcopSkin.addOnClick(innerButton)

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