package river.exertion.kcop.system.messaging.messages

data class StatusViewMessage(val messageType : StatusViewMessageType, val statusKey : String? = null, val statusValue : Float? = null)

enum class StatusViewMessageType {
    ADD_STATUS, REMOVE_STATUS, CLEAR_STATUSES, UPDATE_STATUS
}