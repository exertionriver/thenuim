package river.exertion.kcop.narrative.structure.events

import kotlinx.serialization.Serializable

@Serializable
sealed class Event {
    var id: String? = null
    abstract val type: String

    fun isImageEvent() = this::class.java.interfaces.contains(IImageEvent::class.java)
    fun isMusicEvent() = this::class.java.interfaces.contains(IMusicEvent::class.java)
    fun isSoundEvent() = this::class.java.interfaces.contains(ISoundEvent::class.java)

    fun isLikeImageEvent(event: Event) = (this.isImageEvent() && event.isImageEvent()) &&
            ( (this as IImageEvent).layoutPaneIdx == (event as IImageEvent).layoutPaneIdx )
    fun isLikeMusicEvent(event: Event) = this.isMusicEvent() && event.isMusicEvent()

    fun isLikeEvent(event: Event) = isLikeImageEvent(event) || isLikeMusicEvent(event)

    open fun execEvent(previousEvent : Event? = null) {}
    open fun resolveEvent(currentEvent : Event? = null) {}
}