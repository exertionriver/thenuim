package river.exertion.thenuim.sim.narrative.structure.events

import kotlinx.serialization.Serializable

@Serializable
sealed class Event {
    var id: String? = null
    abstract val type: String

    fun isImageEvent() = this::class.java.interfaces.contains(IImageEvent::class.java)
    fun isMusicEvent() = this::class.java.interfaces.contains(IMusicEvent::class.java)
    fun isSoundEvent() = this::class.java.interfaces.contains(ISoundEvent::class.java)

    fun isLikeImageEvent(event: Event) = (this.isImageEvent() && event.isImageEvent()) &&
            ( (this as IImageEvent).layoutPaneTag == (event as IImageEvent).layoutPaneTag )
    fun isLikeMusicEvent(event: Event) = this.isMusicEvent() && event.isMusicEvent()

    fun isLikeEvent(event: Event) = isLikeImageEvent(event) || isLikeMusicEvent(event)

    open fun execEvent(previousEvent : Event? = null) {}
    open fun resolveEvent(currentEvent : Event? = null) {}
}