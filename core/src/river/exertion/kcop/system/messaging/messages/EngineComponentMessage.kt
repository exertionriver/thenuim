package river.exertion.kcop.system.messaging.messages

import river.exertion.kcop.system.ecs.component.IComponent

data class EngineComponentMessage(val messageType : EngineComponentMessageType, var entityName : String, var componentClass: Class<*>, var componentInstance: IComponent? = null, var initInfo : Any? = null)

//initInfo is component instance if replace

enum class EngineComponentMessageType {
    ADD_COMPONENT, REMOVE_COMPONENT, REPLACE_COMPONENT
}