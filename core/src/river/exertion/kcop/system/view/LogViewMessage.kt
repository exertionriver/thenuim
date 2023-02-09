package river.exertion.kcop.system.view

data class LogViewMessage(val targetView : ViewType = ViewType.LOG, val messageType : LogViewMessageType, val message : String)

enum class LogViewMessageType {
    LogEntry, ImmersionTime, LocalTime
}