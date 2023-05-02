package river.exertion.kcop.view.messaging

import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.view.FontSize

data class DisplayViewTextMessage(val layoutTag: String, val displayText : String? = null, val displayFontSize : FontSize? = null) {

    init {
        MessageChannelHandler.addChannel(MessageChannel(DisplayViewTextBridge, this::class))
    }

    companion object {
        const val DisplayViewTextBridge = "DisplayViewTextBridge"
    }
}