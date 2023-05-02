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
import river.exertion.kcop.view.messaging.MenuViewMessage
import river.exertion.kcop.view.messaging.MenuViewMessage.Companion.MenuViewBridge

class MenuView(screenWidth: Float = 50f, screenHeight: Float = 50f) : Telegraph, ViewBase(ViewType.MENU, screenWidth, screenHeight) {

    init {
        MessageChannelHandler.enableReceive(MenuViewBridge, this)
        MessageChannelHandler.enableReceive(DisplayModeBridge, this)

        MessageChannelHandler.enableReceive(SDCBridge,this)
        MessageChannelHandler.enableReceive(KcopSkinBridge, this)
    }

    var isChecked : MutableMap<Int, Boolean> = mutableMapOf()

    fun buttonLayout() : Table {
        //idx 0 is large button, idx 1 - 5 are smaller bordering buttons
        val buttonList = mutableListOf<Button>()

        (0..5).forEach { idx ->

            val innerButton = Button(skin())//.apply {kcopSkin.addOnEnter(this); kcopSkin.addOnClick(this)}

            if (assignableButtons[idx] == null) {
                assignableButtons[idx] = { MessageChannelHandler.send(MenuViewBridge, MenuViewMessage(null, idx, this@MenuView.isChecked[idx]!!)) }
            }

            //override from ctrl
            innerButton.isChecked = this@MenuView.isChecked[idx] == true

            innerButton.onClick {
                this@MenuView.isChecked[idx] = !(this@MenuView.isChecked[idx] ?: false)

                assignableButtons[idx]?.let { it() }

/*                when (idx) {
                    0 -> {
                        if (this@MenuViewCtrl.isChecked[idx] == true)
                            Switchboard.openMenu()
                        else
                            Switchboard.closeMenu()
                    }
                    1 -> MessageChannelEnum.DISPLAY_MODE_BRIDGE.send(null, this@MenuViewCtrl.isChecked[idx]!!)
                    3 -> MessageChannelEnum.KCOP_BRIDGE.send(null, KcopMessage(KcopMessage.KcopMessageType.FullScreen))
                    4 -> {
                        if (this@MenuViewCtrl.isChecked[idx]!!) {
                            MessageChannelEnum.KCOP_BRIDGE.send(null, KcopMessage(KcopMessage.KcopMessageType.ShowColorPalette))
                        } else {
                            MessageChannelEnum.KCOP_BRIDGE.send(null, KcopMessage(KcopMessage.KcopMessageType.HideColorPalette))
                        }
                    }
                    else -> MessageChannelEnum.DISPLAY_VIEW_MENU_BRIDGE.send(null, DisplayViewMenuMessage(null, idx, this@MenuViewCtrl.isChecked[idx]!!))
                }*/
            }

            buttonList.add(innerButton)
        }

        val buttonSubLayout1 = Table()
        buttonSubLayout1.add(buttonList[1]).size(ViewType.seventhWidth(screenWidth), ViewType.seventhHeight(screenHeight))
        buttonSubLayout1.add(buttonList[2]).size(ViewType.seventhWidth(screenWidth), ViewType.seventhHeight(screenHeight))
        buttonSubLayout1.row()
        buttonSubLayout1.add(buttonList[0]).align(Align.center).colspan(2).size(ViewType.sixthWidth(screenWidth) - 3, ViewType.sixthHeight(screenHeight) - 3)

        val buttonSubLayout2 = Table()
        buttonSubLayout2.add(buttonList[3]).align(Align.center).size(ViewType.seventhWidth(screenWidth) - 5, ViewType.seventhHeight(screenHeight) - 5)
        buttonSubLayout2.row()
        buttonSubLayout2.add(buttonList[4]).size(ViewType.seventhWidth(screenWidth), ViewType.seventhHeight(screenHeight)).padTop(1f)
        buttonSubLayout2.row()
        buttonSubLayout2.add(buttonList[5]).size(ViewType.seventhWidth(screenWidth), ViewType.seventhHeight(screenHeight))

        val buttonLayout = Table()
        buttonLayout.add(buttonSubLayout1).padTop(2f).padLeft(2f).padBottom(2f)
        buttonLayout.add(buttonSubLayout2).padTop(2f).padRight(1f).padBottom(2f)

        buttonLayout.validate()
        buttonLayout.layout()

        return buttonLayout
    }

    override fun buildCtrl() {
        this.add(Stack().apply {
            this.add(backgroundColorImg())
            this.add(buttonLayout())
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
                (MessageChannelHandler.isType(MenuViewBridge, msg.message) ) -> {
                    val menuViewMessage : MenuViewMessage = MessageChannelHandler.receiveMessage(MenuViewBridge, msg.extraInfo)

                    if (menuViewMessage.menuButtonIdx != null) {
                        this@MenuView.isChecked[menuViewMessage.menuButtonIdx] = menuViewMessage.isChecked
                    }

                    build()
                    return true
                }
            }
        }
        return false
    }

    companion object {
        var assignableButtons = mutableMapOf<Int, () -> Unit>()
    }
}