package river.exertion.kcop.simulation.view.ctrl

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import ktx.actors.onClick
import river.exertion.kcop.assets.AssetManagerHandler
import river.exertion.kcop.simulation.view.ViewType
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.AiHintMessage
import river.exertion.kcop.system.messaging.messages.LogViewMessage
import river.exertion.kcop.system.messaging.messages.TextViewMessage

class AiViewCtrl(screenWidth: Float = 50f, screenHeight: Float = 50f) : Telegraph, ViewCtrl(ViewType.AI, screenWidth, screenHeight) {

    init {
        MessageChannel.AI_VIEW_BRIDGE.enableReceive(this)
        MessageChannel.TWO_BATCH_BRIDGE.enableReceive(this)
    }

    var hintTextEntries : MutableMap<String, String> = mutableMapOf()

    fun hintText() = hintTextEntries.values.reduceOrNull { acc, s -> acc + "\n$s"} ?: ""

    var aiUpImage : Texture? = null
    var aiDownImage : Texture? = null
    var aiCheckedImage : Texture? = null

    var isChecked = false

    fun clickButton() : Button {

        var buttonStyle = ButtonStyle()

        if (aiUpImage != null && aiDownImage != null && aiCheckedImage != null) {
            buttonStyle = ButtonStyle(
                TextureRegionDrawable(aiUpImage!!)
                , TextureRegionDrawable(aiDownImage!!)
                , TextureRegionDrawable(aiCheckedImage!!))
        }

        val innerButton = Button(buttonStyle)

        //override from ctrl
        innerButton.isChecked = this@AiViewCtrl.isChecked

        innerButton.onClick {
            this@AiViewCtrl.isChecked = !this@AiViewCtrl.isChecked
            MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessage.LogViewMessageType.LogEntry, "AI character set to: ${if (this.isChecked) "On" else "Off"}" ))

            if (isChecked) {
                MessageChannel.TEXT_VIEW_BRIDGE.send(null, TextViewMessage(TextViewMessage.TextViewMessageType.HintText, hintText()))
            } else {
                MessageChannel.TEXT_VIEW_BRIDGE.send(null, TextViewMessage(TextViewMessage.TextViewMessageType.HintText))
            }
        }

        return innerButton
    }

    override fun buildCtrl() {
        this.add(clickButton()).width(this.tableWidth()).height(this.tableHeight())
        this.clip()
    }

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                (MessageChannel.TWO_BATCH_BRIDGE.isType(msg.message) ) -> {
                    val twoBatch: PolygonSpriteBatch = MessageChannel.TWO_BATCH_BRIDGE.receiveMessage(msg.extraInfo)
                    super.batch = twoBatch
                    return true
                }
                (MessageChannel.AI_VIEW_BRIDGE.isType(msg.message) ) -> {
                    val aiHintMessage: AiHintMessage = MessageChannel.AI_VIEW_BRIDGE.receiveMessage(msg.extraInfo)

                    when (aiHintMessage.aiHintMessageType) {
                        AiHintMessage.AiHintMessageType.ClearHints -> hintTextEntries.clear()
                        AiHintMessage.AiHintMessageType.AddHint -> {
                            if (aiHintMessage.aiHintEventId != null && aiHintMessage.aiHintEventReport != null)
                                hintTextEntries[aiHintMessage.aiHintEventId] = aiHintMessage.aiHintEventReport
                        }
                    }

                    if (isChecked) {
                        MessageChannel.TEXT_VIEW_BRIDGE.send(null, TextViewMessage(TextViewMessage.TextViewMessageType.HintText, hintText()))
                    } else {
                        MessageChannel.TEXT_VIEW_BRIDGE.send(null, TextViewMessage(TextViewMessage.TextViewMessageType.HintText))
                    }

                    return true
                }
            }
        }
        return false
    }
}