package river.exertion.kcop.system.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import ktx.ashley.oneOf
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.ecs.component.IRLTimeComponent
import river.exertion.kcop.system.ecs.component.ImmersionTimerComponent
import river.exertion.kcop.system.ecs.component.NarrativeComponent
import river.exertion.kcop.system.ecs.component.ProfileComponent
import river.exertion.kcop.system.messaging.messages.AMHSaveMessage
import river.exertion.kcop.system.messaging.messages.LogViewMessage
import river.exertion.kcop.system.messaging.messages.LogViewMessageType
import river.exertion.kcop.system.messaging.messages.ProfileMessage

class AMHUpdateSystem : IntervalIteratingSystem(oneOf(ProfileComponent::class, NarrativeComponent::class).get(), 1f) {

    override fun processEntity(entity: Entity) {

        val profileComponent = ProfileComponent.getFor(entity)
        val narrativeComponent = NarrativeComponent.getFor(entity)

        if ( profileComponent != null ) {
            MessageChannel.PROFILE_BRIDGE.send(null, ProfileMessage(ProfileMessage.ProfileMessageType.UPDATE_CUML_TIME, null, profileComponent.cumlTime()))
            MessageChannel.AMH_SAVE_BRIDGE.send(null, AMHSaveMessage(AMHSaveMessage.AMHSaveMessageType.ReloadCurrentProfile, profileComponent))
        }
        if ( narrativeComponent != null ) {
            MessageChannel.AMH_SAVE_BRIDGE.send(null, AMHSaveMessage(AMHSaveMessage.AMHSaveMessageType.ReloadCurrentImmersion, null, narrativeComponent))
       }
    }
}