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
import river.exertion.kcop.view.messaging.DisplayModeMessage.Companion.DisplayModeBridge
import river.exertion.kcop.view.messaging.PauseViewMessage
import river.exertion.kcop.view.messaging.PauseViewMessage.Companion.PauseViewBridge

class PauseView(screenWidth: Float = 50f, screenHeight: Float = 50f) : Telegraph, ViewBase(ViewType.PAUSE, screenWidth, screenHeight) {

    init {
        MessageChannelHandler.enableReceive(PauseViewBridge, this)
//        MessageChannelEnum.NARRATIVE_BRIDGE_PAUSE_GATE.enableReceive(this)
        MessageChannelHandler.enableReceive(DisplayModeBridge, this)

        MessageChannelHandler.enableReceive(SDCBridge,this)
        MessageChannelHandler.enableReceive(KcopSkinBridge, this)
    }

    var isChecked = false

    fun clickButton() : Button {

        val innerButton = Button(skin())
        //override from ctrl
        innerButton.isChecked = this@PauseView.isChecked

        innerButton.onClick {
            this@PauseView.isChecked = !this@PauseView.isChecked
            toggleImmersionPause()
        }

        return innerButton
    }

    fun toggleImmersionPause() {
//        val messageType = if (isChecked) NarrativeMessage.NarrativeMessageType.Pause else NarrativeMessage.NarrativeMessageType.Unpause
//        MessageChannelEnum.NARRATIVE_BRIDGE.send(null, NarrativeMessage(messageType, null) )
    }

    override fun buildCtrl() {
        this.add(Stack().apply {
            this.add(backgroundColorImg())
            this.add(Table().apply {
                this.add(clickButton()).align(Align.center).size(this@PauseView.tableWidth() - 5, this@PauseView.tableHeight() - 5)
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
                (MessageChannelHandler.isType(PauseViewBridge, msg.message) ) -> {
                    val pauseViewMessage : PauseViewMessage = MessageChannelHandler.receiveMessage(PauseViewBridge, msg.extraInfo)

                    if (isChecked != pauseViewMessage.setPause) {
                        isChecked = pauseViewMessage.setPause
                        toggleImmersionPause()
                    }

                    build()
                    return true
                }
/*                (MessageChannelEnum.NARRATIVE_BRIDGE_PAUSE_GATE.isType(msg.message) ) -> {
                    val narrativeMessage: NarrativeMessage = MessageChannelEnum.NARRATIVE_BRIDGE_PAUSE_GATE.receiveMessage(msg.extraInfo)

                    if (!isChecked) {
                        MessageChannelEnum.NARRATIVE_BRIDGE.send(null, narrativeMessage)
                    }
                    return true
                }
*/            }
        }
        return false
    }
}