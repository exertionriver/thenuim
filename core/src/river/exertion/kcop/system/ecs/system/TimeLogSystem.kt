package river.exertion.kcop.system.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import ktx.ashley.oneOf
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.ecs.component.IRLTimeComponent
import river.exertion.kcop.system.ecs.component.ImmersionTimerComponent
import river.exertion.kcop.system.messaging.messages.LogViewMessage
import river.exertion.kcop.system.messaging.messages.LogViewMessageType

class TimeLogSystem : IntervalIteratingSystem(oneOf(ImmersionTimerComponent::class, IRLTimeComponent::class).get(), 1/60f) {

    override fun processEntity(entity: Entity) {

        val immersionTimerComponent = ImmersionTimerComponent.getFor(entity)
        val irlTimeComponent = IRLTimeComponent.getFor(entity)

        if (irlTimeComponent != null) {
            MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.LocalTime, irlTimeComponent.localTime()))
        }

        if ( immersionTimerComponent != null ) {
            MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.InstImmersionTime, immersionTimerComponent.instImmersionTime()) )
            MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.CumlImmersionTime, immersionTimerComponent.cumlImmersionTime()) )
        }
    }
}