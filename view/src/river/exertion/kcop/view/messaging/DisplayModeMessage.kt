package river.exertion.kcop.view.messaging

import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler

data class DisplayModeMessage(val isDisplayMode : Boolean) {

    init {
        MessageChannelHandler.addChannel(MessageChannel(DisplayModeBridge, this::class))
    }

    companion object {
        const val DisplayModeBridge = "DisplayModeBridge"
    }
}