package river.exertion.kcop.ecs.messaging

import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler

data class EngineEntityMessage(val messageType : EngineEntityMessageType, var entityClass : Class<*>, var initInfo : Any? = null) {

    init {
        MessageChannelHandler.addChannel(MessageChannel(EngineEntityBridge, this::class))
    }

    enum class EngineEntityMessageType {
        InstantiateEntity, RemoveEntity, RemoveAllEntities
    }

    companion object {
        const val EngineEntityBridge = "EngineEntityBridge"
    }
}

