package river.exertion.kcop.narrative.structure

import kotlinx.serialization.Serializable

@Serializable
data class Event(
    var id: String? = null,
    val eventType: String = "",
    val eventTrigger: String = "",
    val param: String = "",
    val param2: String = ""
) {

    @Transient
    var fired = false

    fun validateFields() : Boolean {
        return EventType.isEventType(eventType)
    }

    //after running validation at load-time
    fun eventType() : EventType? = EventType.values().firstOrNull { eventType == it.label() }

    enum class EventType {
        LOG { override fun label() = "log"},
        TEXT { override fun label() = "text"},
        SET_FLAG { override fun label() = "setFlag"},
        UNSET_FLAG {override fun label() = "unsetFlag"},
        ZERO_COUNTER {override fun label() = "zeroCounter"},
        PLUS_COUNTER {override fun label() = "plusCounter"},
        MINUS_COUNTER {override fun label() = "minusCounter"},
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
            fun isSoundEvent(event : String) = listOf(PLAY_SOUND.label()).contains(event)
        }
    }

    fun eventTrigger() : EventTrigger? = EventTrigger.values().firstOrNull { eventTrigger == it.label() }

    enum class EventTrigger {
        ON_EXIT { override fun label() = "onExit"},
        ON_ENTRY {override fun label() = "onEntry"},
        ;
        abstract fun label() : String
    }
}