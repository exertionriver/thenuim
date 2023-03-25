package river.exertion.kcop.simulation.view.ctrl

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import ktx.actors.onClick
import river.exertion.kcop.simulation.view.ViewType
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.NarrativeMessage
import river.exertion.kcop.system.messaging.messages.PauseViewMessage

class PauseViewCtrl(screenWidth: Float = 50f, screenHeight: Float = 50f) : Telegraph, ViewCtrl(ViewType.PAUSE, screenWidth, screenHeight) {

    init {
        MessageChannel.PAUSE_VIEW_BRIDGE.enableReceive(this)
        MessageChannel.NARRATIVE_BRIDGE_PAUSE_GATE.enableReceive(this)
    }

    var pauseUpImage : Texture? = null
    var pauseDownImage : Texture? = null
    var pauseCheckedImage : Texture? = null

    var isChecked = false

    fun clickButton() : Button {

        var buttonStyle = ButtonStyle()

        if (pauseUpImage != null && pauseDownImage != null && pauseCheckedImage != null) {
            buttonStyle = ButtonStyle(
                TextureRegionDrawable(pauseUpImage!!)
                , TextureRegionDrawable(pauseDownImage!!)
                , TextureRegionDrawable(pauseCheckedImage!!))
        }

        val innerButton = Button(buttonStyle)

        //override from ctrl
        innerButton.isChecked = this@PauseViewCtrl.isChecked

        innerButton.onClick {
            this@PauseViewCtrl.isChecked = !this@PauseViewCtrl.isChecked

            val messageType = if (isChecked) NarrativeMessage.NarrativeMessageType.PAUSE else NarrativeMessage.NarrativeMessageType.UNPAUSE
            MessageChannel.NARRATIVE_BRIDGE.send(null, NarrativeMessage(messageType, null) )
        }

        return innerButton
    }

    override fun build(bitmapFont: BitmapFont, batch: Batch) {
        this.add(clickButton()).width(this.tableWidth()).height(this.tableHeight())
        this.clip()
    }

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            if (MessageChannel.PAUSE_VIEW_BRIDGE.isType(msg.message) ) {
                val pauseMessage : PauseViewMessage = MessageChannel.PAUSE_VIEW_BRIDGE.receiveMessage(msg.extraInfo)

                if (!isChecked) {
                    isChecked = pauseMessage.setPause
                }

                if (isInitialized) recreate()
                return true
            }
            if (MessageChannel.NARRATIVE_BRIDGE_PAUSE_GATE.isType(msg.message) ) {
                val narrativeMessage: NarrativeMessage = MessageChannel.NARRATIVE_BRIDGE_PAUSE_GATE.receiveMessage(msg.extraInfo)

                if (!isChecked) {
                    MessageChannel.NARRATIVE_BRIDGE.send(null, narrativeMessage)
                }
            }
        }
        return false
    }
}