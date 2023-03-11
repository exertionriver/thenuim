package river.exertion.kcop.system

data class EngineMessage(val messageType : EngineMessageType, var entityClass : Class<*>, var initInfo : Any? = null)

enum class EngineMessageType {
    INSTANTIATE_ENTITY, ADD_COMPONENT
}