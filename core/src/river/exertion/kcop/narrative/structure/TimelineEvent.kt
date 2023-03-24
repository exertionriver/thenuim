package river.exertion.kcop.narrative.structure

import kotlinx.serialization.Serializable
import river.exertion.kcop.system.immersionTimer.ImmersionTimer

@Serializable
data class TimelineEvent(
    var id : String? = null,
    val immersionTime: String = "",
    val event: String = "",
    val param: String = ""
) {

    fun validateFields() : Boolean {
        return TimelineEventType.isTimelineEventType(event)
    }

    //after running validation at load-time
    fun timelineEventType() : TimelineEventType = TimelineEventType.values().first { event == it.label() }

    enum class TimelineEventType {
        LOG { override fun label() = "log"},
        TEXT {override fun label() = "text"}
        ;
        abstract fun label() : String

        companion object {
            fun isTimelineEventType(event : String) = values().firstOrNull { it.label() == event } != null
        }
    }
}