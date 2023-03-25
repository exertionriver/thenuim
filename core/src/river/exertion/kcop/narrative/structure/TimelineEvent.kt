package river.exertion.kcop.narrative.structure

import kotlinx.serialization.Serializable

@Serializable
data class TimelineEvent(
    var id : String? = null,
    val immersionTime: String = "",
    val eventType: String = "",
    val param: String = ""
) {

    fun validateFields() : Boolean {
        return Event.EventType.isEventType(eventType)
    }

    //after running validation at load-time
    fun eventType() : Event.EventType = Event.EventType.values().first { eventType == it.label() }

}