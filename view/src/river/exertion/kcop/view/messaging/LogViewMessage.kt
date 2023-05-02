package river.exertion.kcop.view.messaging

import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler

data class LogViewMessage(val messageType : LogViewMessageType, val message : String? = null, val param : String? = null) {

    init {
        MessageChannelHandler.addChannel(MessageChannel(LogViewBridge, this::class))
    }

    enum class LogViewMessageType {
        LogEntry, ResetTime, InstImmersionTime, CumlImmersionTime, LocalTime
    }

    companion object {
        const val LogViewBridge = "LogViewBridge"
    }
}

