package river.exertion.kcop.view.switchboard

import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.messaging.Switchboard
import river.exertion.kcop.messaging.SwitchboardEntry
import river.exertion.kcop.view.messaging.MenuViewMessage
import river.exertion.kcop.view.messaging.PauseViewMessage

object MenuViewSwitchboard {

    val CloseMenu = SwitchboardEntry("CloseMenu")

    fun closeMenu() {
        if (Switchboard.checkByTag(CloseMenu.switchboardTag) == null) {
            Switchboard.addEntry(CloseMenu.apply { this.switchboardTagAction = {
                MessageChannelHandler.send(MenuViewMessage.MenuViewBridge, MenuViewMessage(null, 0, false))
                MessageChannelHandler.send(PauseViewMessage.PauseViewBridge, PauseViewMessage(false))
            } } )
        } else {
            Switchboard.executeAction(CloseMenu.switchboardTag)
        }
    }
}