package river.exertion.kcop.simulation.view.ctrl

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import ktx.actors.onClick
import river.exertion.kcop.simulation.view.ViewType
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.NarrativeMessage
import river.exertion.kcop.system.messaging.messages.PauseViewMessage

class PauseViewCtrl(screenWidth: Float = 50f, screenHeight: Float = 50f) : Telegraph, ViewCtrl(ViewType.PAUSE, screenWidth, screenHeight) {

    init {
        MessageChannel.PAUSE_VIEW_BRIDGE.enableReceive(this)
        MessageChannel.NARRATIVE_BRIDGE_PAUSE_GATE.enableReceive(this)

        MessageChannel.SDC_BRIDGE.enableReceive(this)
        MessageChannel.KCOP_SKIN_BRIDGE.enableReceive(this)
    }

    var isChecked = false

    fun clickButton() : Button {

        val innerButton = Button(skin())
        //override from ctrl
        innerButton.isChecked = this@PauseViewCtrl.isChecked

        innerButton.onClick {
            this@PauseViewCtrl.isChecked = !this@PauseViewCtrl.isChecked
            toggleImmersionPause()
        }

        return innerButton
    }

    fun toggleImmersionPause() {
        val messageType = if (isChecked) NarrativeMessage.NarrativeMessageType.Pause else NarrativeMessage.NarrativeMessageType.Unpause
        MessageChannel.NARRATIVE_BRIDGE.send(null, NarrativeMessage(messageType, null) )
    }

    override fun buildCtrl() {
        this.add(Stack().apply {
            this.add(backgroundColorImg())
            this.add(Table().apply {
                this.add(clickButton()).align(Align.center).size(this@PauseViewCtrl.tableWidth() - 5, this@PauseViewCtrl.tableHeight() - 5)
            })
        } ).size(this.tableWidth(), this.tableHeight())

        this.clip()
    }

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            when {
                (MessageChannel.SDC_BRIDGE.isType(msg.message) ) -> {
                    super.sdcHandler = MessageChannel.SDC_BRIDGE.receiveMessage(msg.extraInfo)
                    return true
                }

                (MessageChannel.KCOP_SKIN_BRIDGE.isType(msg.message) ) -> {
                    super.kcopSkin = MessageChannel.KCOP_SKIN_BRIDGE.receiveMessage(msg.extraInfo)
                    return true
                }
                (MessageChannel.PAUSE_VIEW_BRIDGE.isType(msg.message) ) -> {
                    val pauseMessage : PauseViewMessage = MessageChannel.PAUSE_VIEW_BRIDGE.receiveMessage(msg.extraInfo)

                    if (isChecked != pauseMessage.setPause) {
                        isChecked = pauseMessage.setPause
                        toggleImmersionPause()
                    }

                    build()
                    return true
                }
                (MessageChannel.NARRATIVE_BRIDGE_PAUSE_GATE.isType(msg.message) ) -> {
                    val narrativeMessage: NarrativeMessage = MessageChannel.NARRATIVE_BRIDGE_PAUSE_GATE.receiveMessage(msg.extraInfo)

                    if (!isChecked) {
                        MessageChannel.NARRATIVE_BRIDGE.send(null, narrativeMessage)
                    }
                    return true
                }
            }
        }
        return false
    }
}