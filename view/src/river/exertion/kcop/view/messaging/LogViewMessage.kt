package river.exertion.kcop.view.messaging

data class LogViewMessage(val messageType : LogViewMessageType, val message : String? = null, val param : String? = null) {

    enum class LogViewMessageType {
        LogEntry, ResetTime, InstImmersionTime, CumlImmersionTime, LocalTime
    }
}

