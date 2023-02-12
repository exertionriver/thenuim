package river.exertion.kcop.system.view

data class LogViewMessage(val messageType : LogViewMessageType, val message : String)

enum class LogViewMessageType {
    LogEntry, ImmersionTime, LocalTime
}