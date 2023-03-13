package river.exertion.kcop.system.messaging.messages

data class StatusViewMessage(val messageType : StatusViewMessageType, val statusKey : String, val statusValue : Float? = null)

enum class StatusViewMessageType {
    ADD_STATUS, REMOVE_STATUS, UPDATE_STATUS
}