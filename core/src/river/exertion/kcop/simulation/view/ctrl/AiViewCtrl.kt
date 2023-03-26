package river.exertion.kcop.simulation.view.ctrl

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import ktx.actors.onClick
import river.exertion.kcop.simulation.view.ViewType
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.LogViewMessage
import river.exertion.kcop.system.messaging.messages.LogViewMessageType

class AiViewCtrl(screenWidth: Float = 50f, screenHeight: Float = 50f) : Telegraph, ViewCtrl(ViewType.AI, screenWidth, screenHeight) {

    init {
        MessageChannel.AI_VIEW_BRIDGE.enableReceive(this)
        MessageChannel.TWO_BATCH_BRIDGE.enableReceive(this)
    }

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
            MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.LogEntry, "AI set to: ${if (this.isChecked) "On" else "Off"}" ))
        }

        return innerButton
    }

    override fun build(bitmapFont: BitmapFont, batch: Batch) {
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
//                val logMessage : LogViewMessage = MessageChannel.LOG_VIEW_BRIDGE.receiveMessage(msg.extraInfo)

                    if (isInitialized) recreate()
                    return true
                }
            }
        }
        return false
    }
}