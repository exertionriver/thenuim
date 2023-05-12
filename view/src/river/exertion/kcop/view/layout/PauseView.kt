package river.exertion.kcop.view.layout

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import ktx.actors.onClick
import river.exertion.kcop.asset.view.KcopSkin
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.view.ViewPackage.ImmersionKeypressBridge
import river.exertion.kcop.view.ViewPackage.ImmersionPauseBridge
import river.exertion.kcop.view.ViewPackage.PauseViewBridge
import river.exertion.kcop.view.messaging.PauseViewMessage

class PauseView : Telegraph, ViewBase(ViewType.PAUSE) {

    init {
        MessageChannelHandler.enableReceive(PauseViewBridge, this)
        MessageChannelHandler.enableReceive(ImmersionPauseBridge, this)
    }

    var isChecked = false

    fun clickButton() : Button {

        val innerButton = Button(KcopSkin.skin)
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
                (MessageChannelHandler.isType(PauseViewBridge, msg.message) ) -> {
                    val pauseViewMessage : PauseViewMessage = MessageChannelHandler.receiveMessage(PauseViewBridge, msg.extraInfo)

                    if (isChecked != pauseViewMessage.setPause) {
                        isChecked = pauseViewMessage.setPause
                        toggleImmersionPause()
                    }

                    build()
                    return true
                }
                (MessageChannelHandler.isType(ImmersionPauseBridge, msg.message) ) -> {
                    val keyString: String = MessageChannelHandler.receiveMessage(ImmersionPauseBridge, msg.extraInfo)

                    if (!isChecked) {
                        MessageChannelHandler.send(ImmersionKeypressBridge, keyString)
                    }
                    return true
                }
            }
        }
        return false
    }
}