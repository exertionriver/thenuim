package river.exertion.kcop.narrative.structure

import kotlinx.serialization.Serializable
import river.exertion.kcop.Id

@Serializable
data class Event(
    var id: String? = null,
    val event: String = "",
    val trigger: String = "",
    val param: String = ""
) {

    fun validateFields() : Boolean {
        return EventType.isEventType(event)
    }

    //after running validation at load-time
    fun event() : EventType? = EventType.values().firstOrNull { event == it.label() }

    enum class EventType {
        LOG { override fun label() = "log"},
        TEXT {override fun label() = "text"},
        SET_FLAG { override fun label() = "setFlag"},
        GET_FLAG {override fun label() = "getFlag"}
        ;
        abstract fun label() : String

        companion object {
            fun isEventType(event : String) = values().firstOrNull { it.label() == event } != null
        }
    }
}