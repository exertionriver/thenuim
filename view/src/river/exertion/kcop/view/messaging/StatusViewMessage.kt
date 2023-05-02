package river.exertion.kcop.view.messaging

import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler

data class StatusViewMessage(val messageType : StatusViewMessageType, val statusKey : String? = null, val statusValue : Float? = null) {

    init {
        MessageChannelHandler.addChannel(MessageChannel(StatusViewBridge, this::class))
    }

    enum class StatusViewMessageType {
        AddStatus, UpdateStatus, RemoveStatus, ClearStatuses
    }

    companion object {
        const val StatusViewBridge = "StatusViewBridge"
    }
}

