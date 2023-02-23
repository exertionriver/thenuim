package river.exertion.kcop.narrative.structure

import kotlinx.serialization.Serializable

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
        GET_FLAG {override fun label() = "getFlag"},
        SHOW_IMAGE_LARGE {override fun label() = "showImageLarge"},
        SHOW_IMAGE_MEDIUM {override fun label() = "showImageMedium"},
        SHOW_IMAGE_SMALL {override fun label() = "showImageSmall"},
        SHOW_IMAGE_TINY {override fun label() = "showImageTiny"},
        PLAY_SOUND {override fun label() = "playSound"},
        PLAY_MUSIC {override fun label() = "playMusic"}
        ;
        abstract fun label() : String

        companion object {
            fun isEventType(event : String) = values().firstOrNull { it.label() == event } != null
            fun isImageEvent(event : String) = listOf(SHOW_IMAGE_LARGE.label(), SHOW_IMAGE_MEDIUM.label(), SHOW_IMAGE_SMALL.label(), SHOW_IMAGE_TINY.label()).contains(event)
            fun isAudioEvent(event : String) = listOf(PLAY_SOUND.label(), PLAY_MUSIC.label()).contains(event)
        }
    }
}