package river.exertion.kcop.system.messaging.messages

data class EngineComponentMessage(val messageType : EngineComponentMessageType, var entityId : String, var componentClass: Class<*>, var initInfo : Any? = null)

//initInfo is component instance if replace

enum class EngineComponentMessageType {
    ADD_COMPONENT, REMOVE_COMPONENT, REPLACE_COMPONENT
}