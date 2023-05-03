package river.exertion.kcop.sim.menu.messaging

import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.sim.menu.params.MenuNavParams

data class MenuNavMessage(var menuNavParams: MenuNavParams? = null) {

    init {
        MessageChannelHandler.addChannel(MessageChannel(MenuNavBridge, this::class))
    }

    companion object {
        const val MenuNavBridge = "MenuNavBridge"
    }
}
