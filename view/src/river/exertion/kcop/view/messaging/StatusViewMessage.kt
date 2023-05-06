package river.exertion.kcop.view.messaging

import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler

data class StatusViewMessage(val messageType : StatusViewMessageType, val statusKey : String? = null, val statusValue : Float? = null) {

    enum class StatusViewMessageType {
        AddStatus, UpdateStatus, RemoveStatus, ClearStatuses
    }
}

