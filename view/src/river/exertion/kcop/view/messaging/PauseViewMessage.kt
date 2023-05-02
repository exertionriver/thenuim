package river.exertion.kcop.view.messaging

import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler

data class PauseViewMessage(val setPause : Boolean) {

    init {
        MessageChannelHandler.addChannel(MessageChannel(PauseViewBridge, this::class))
    }

    companion object {
        const val PauseViewBridge = "PauseViewBridge"
    }
}