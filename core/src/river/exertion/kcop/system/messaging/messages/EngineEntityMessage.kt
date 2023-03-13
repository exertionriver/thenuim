package river.exertion.kcop.system.messaging.messages

data class EngineEntityMessage(val messageType : EngineEntityMessageType, var entityClass : Class<*>, var initInfo : Any? = null)

enum class EngineEntityMessageType {
    INSTANTIATE_ENTITY, REMOVE_ENTITY, REMOVE_ALL_ENTITIES
}