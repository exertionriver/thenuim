package river.exertion.kcop.view.switchboard

import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.messaging.Switchboard
import river.exertion.kcop.messaging.SwitchboardEntry
import river.exertion.kcop.view.ViewPackage.MenuNavBridge
import river.exertion.kcop.view.ViewPackage.MenuViewBridge
import river.exertion.kcop.view.ViewPackage.PauseViewBridge
import river.exertion.kcop.view.menu.MainMenu
import river.exertion.kcop.view.messaging.MenuNavMessage
import river.exertion.kcop.view.messaging.MenuViewMessage
import river.exertion.kcop.view.messaging.PauseViewMessage

object MenuViewSwitchboard {

    val OpenMenu = SwitchboardEntry("OpenMenu")
    val CloseMenu = SwitchboardEntry("CloseMenu")
    val ClearMenu = SwitchboardEntry("ClearMenu")

    fun openMenu() {
        if (Switchboard.checkByTag(OpenMenu.switchboardTag) == null) {
            Switchboard.addEntry(OpenMenu.apply { this.switchboardTagAction = {
                clearMenu()
                MessageChannelHandler.send(PauseViewBridge, PauseViewMessage(true))
                MessageChannelHandler.send(MenuViewBridge, MenuViewMessage(null, 0, true))
            } } )
        }
        Switchboard.executeAction(OpenMenu.switchboardTag)
    }

    fun closeMenu() {
        if (Switchboard.checkByTag(CloseMenu.switchboardTag) == null) {
            Switchboard.addEntry(CloseMenu.apply { this.switchboardTagAction = {
                MessageChannelHandler.send(MenuViewBridge, MenuViewMessage(null, 0, false))
                MessageChannelHandler.send(PauseViewBridge, PauseViewMessage(false))
            } } )
        }
        Switchboard.executeAction(CloseMenu.switchboardTag)
    }

    fun clearMenu() {
        if (Switchboard.checkByTag(ClearMenu.switchboardTag) == null) {
            Switchboard.addEntry(ClearMenu.apply { this.switchboardTagAction = {
                MessageChannelHandler.send(MenuViewBridge, MenuViewMessage(MainMenu.tag))
                MessageChannelHandler.send(MenuNavBridge, MenuNavMessage())
            } } )
        }
        Switchboard.executeAction(CloseMenu.switchboardTag)
    }

}