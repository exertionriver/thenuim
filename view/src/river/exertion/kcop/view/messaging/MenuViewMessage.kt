package river.exertion.kcop.view.messaging

import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler

data class MenuViewMessage(val targetMenuTag : String? = null, val menuButtonIdx : Int? = null, var isChecked : Boolean = false) {

    init {
        MessageChannelHandler.addChannel(MessageChannel(MenuViewBridge, this::class))
    }

    companion object {
        const val MenuViewBridge = "MenuViewBridge"
    }
}
