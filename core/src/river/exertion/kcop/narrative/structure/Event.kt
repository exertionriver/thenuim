package river.exertion.kcop.narrative.structure

import kotlinx.serialization.Serializable

@Serializable
data class Event(
    var id: String? = null,
    val event: String = "",
    val trigger: String = "",
    val param: String = "",
    val param2: String = ""
) {

    fun validateFields() : Boolean {
        return EventType.isEventType(event)
    }

    //after running validation at load-time
    fun eventType() : EventType? = EventType.values().firstOrNull { event == it.label() }

    enum class EventType {
        LOG { override fun label() = "log"},
        TEXT {override fun label() = "text"},
        SET_FLAG { override fun label() = "setFlag"},
        GET_FLAG {override fun label() = "getFlag"},
        SHOW_IMAGE {override fun label() = "showImage"},
        FADE_IMAGE {override fun label() = "fadeImage"},
        PLAY_SOUND {override fun label() = "playSound"},
        PLAY_MUSIC {override fun label() = "playMusic"},
        FADE_MUSIC {override fun label() = "fadeMusic"}
        ;
        abstract fun label() : String

        companion object {
            fun isEventType(event : String) = values().firstOrNull { it.label() == event } != null
            fun isImageEvent(event : String) = listOf(SHOW_IMAGE.label(), FADE_IMAGE.label()).contains(event)
            fun isMusicEvent(event : String) = listOf(PLAY_MUSIC.label(), FADE_MUSIC.label()).contains(event)
        }
    }

    fun eventTrigger() : EventTrigger? = EventTrigger.values().firstOrNull { trigger == it.label() }

    enum class EventTrigger {
        ON_EXIT { override fun label() = "onExit"},
        ON_ENTRY {override fun label() = "onEntry"},
        ;
        abstract fun label() : String
    }
}