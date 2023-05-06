package river.exertion.kcop.ecs.messaging

import river.exertion.kcop.messaging.MessageChannel
import river.exertion.kcop.messaging.MessageChannelHandler

data class EngineComponentMessage(val messageType : EngineComponentMessageType, var entityName : String, var componentClass: Class<*>, var initInfo : Any? = null) {

    enum class EngineComponentMessageType {
        AddComponent, RemoveComponent, ReplaceComponent
    }
}

