package river.exertion.kcop.sim.menu.messaging

import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.sim.menu.params.NarrativeMenuDataParams
import river.exertion.kcop.sim.menu.params.ProfileMenuDataParams

data class MenuDataMessage(var profileMenuDataParams: ProfileMenuDataParams? = null, var narrativeMenuDataParams: NarrativeMenuDataParams? = null) {

    init {
        MessageChannelHandler.addChannel(MessageChannel(MenuDataBridge, this::class))
    }

    companion object {
        const val MenuDataBridge = "MenuDataBridge"
    }
}
