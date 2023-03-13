package river.exertion.kcop.system.messaging.messages

data class EngineComponentMessage(val messageType : EngineComponentMessageType, var entityId : String, var componentClass: Class<*>, var initInfo : Any? = null)

enum class EngineComponentMessageType {
    ADD_COMPONENT, REMOVE_COMPONENT
}