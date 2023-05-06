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
import river.exertion.kcop.view.ViewPackage.KcopBridge
import river.exertion.kcop.view.ViewPackage.MenuViewBridge
import river.exertion.kcop.view.messaging.KcopMessage
import river.exertion.kcop.view.messaging.MenuViewMessage
import river.exertion.kcop.view.switchboard.MenuViewSwitchboard

class MenuView : Telegraph, ViewBase(ViewType.MENU) {

    init {
        MessageChannelHandler.enableReceive(MenuViewBridge, this)

        assignableButtons[0] = {
        if (this@MenuView.isChecked[0] == true)
            MenuViewSwitchboard.openMenu()
        else
            MenuViewSwitchboard.closeMenu()
        }

        assignableButtons[1] = {
            KcopSkin.displayMode = this@MenuView.isChecked[1]!!
        }

        assignableButtons[2] = {
            MessageChannelHandler.send(MenuViewBridge, MenuViewMessage(null, 2, this@MenuView.isChecked[2]!!))
        }

        assignableButtons[3] = {
            MessageChannelHandler.send(KcopBridge, KcopMessage(KcopMessage.KcopMessageType.FullScreen))
        }

        assignableButtons[4] = {
            MessageChannelHandler.send(MenuViewBridge, MenuViewMessage(null, 4, this@MenuView.isChecked[4]!!))
        }

        assignableButtons[5] = {
            MessageChannelHandler.send(MenuViewBridge, MenuViewMessage(null, 5, this@MenuView.isChecked[5]!!))
        }

    }

    var isChecked : MutableMap<Int, Boolean> = mutableMapOf()

    fun buttonLayout() : Table {
        //idx 0 is large button, idx 1 - 5 are smaller bordering buttons
        val buttonList = mutableListOf<Button>()

        (0..5).forEach { idx ->

            val innerButton = Button(KcopSkin.skin).apply { KcopSkin.addOnClick(this) }

            if (assignableButtons[idx] == null) {
                assignableButtons[idx] = { MessageChannelHandler.send(MenuViewBridge, MenuViewMessage(null, idx, this@MenuView.isChecked[idx]!!)) }
            }

            //override from ctrl
            innerButton.isChecked = this@MenuView.isChecked[idx] == true

            innerButton.onClick {
                this@MenuView.isChecked[idx] = !(this@MenuView.isChecked[idx] ?: false)

                assignableButtons[idx]?.let { it() }
            }

            buttonList.add(innerButton)
        }

        val buttonSubLayout1 = Table()
        buttonSubLayout1.add(buttonList[1]).size(ViewType.seventhWidth(KcopSkin.screenWidth), ViewType.seventhHeight(KcopSkin.screenHeight))
        buttonSubLayout1.add(buttonList[2]).size(ViewType.seventhWidth(KcopSkin.screenWidth), ViewType.seventhHeight(KcopSkin.screenHeight))
        buttonSubLayout1.row()
        buttonSubLayout1.add(buttonList[0]).align(Align.center).colspan(2).size(ViewType.sixthWidth(KcopSkin.screenWidth) - 3, ViewType.sixthHeight(KcopSkin.screenHeight) - 3)

        val buttonSubLayout2 = Table()
        buttonSubLayout2.add(buttonList[3]).align(Align.center).size(ViewType.seventhWidth(KcopSkin.screenWidth) - 5, ViewType.seventhHeight(KcopSkin.screenHeight) - 5)
        buttonSubLayout2.row()
        buttonSubLayout2.add(buttonList[4]).size(ViewType.seventhWidth(KcopSkin.screenWidth), ViewType.seventhHeight(KcopSkin.screenHeight)).padTop(1f)
        buttonSubLayout2.row()
        buttonSubLayout2.add(buttonList[5]).size(ViewType.seventhWidth(KcopSkin.screenWidth), ViewType.seventhHeight(KcopSkin.screenHeight))

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