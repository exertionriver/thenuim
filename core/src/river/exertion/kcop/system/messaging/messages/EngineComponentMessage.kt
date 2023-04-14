package river.exertion.kcop.system.messaging.messages

data class EngineComponentMessage(val messageType : EngineComponentMessageType, var entityName : String, var componentClass: Class<*>, var initInfo : Any? = null) {

    enum class EngineComponentMessageType {
        AddComponent, RemoveComponent, ReplaceComponent
    }
}

