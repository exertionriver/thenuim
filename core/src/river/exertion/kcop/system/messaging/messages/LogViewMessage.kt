package river.exertion.kcop.system.messaging.messages

data class LogViewMessage(val messageType : LogViewMessageType, val message : String? = null, val param : String? = null)

enum class LogViewMessageType {
    LogEntry, ResetTime, InstImmersionTime, CumlImmersionTime, LocalTime
}