package river.exertion.kcop.system.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import ktx.ashley.oneOf
import river.exertion.kcop.system.ecs.component.IRLTimeComponent
import river.exertion.kcop.system.ecs.component.ImmersionTimerComponent
import river.exertion.kcop.system.messaging.MessageChannelEnum
import river.exertion.kcop.view.messaging.LogViewMessage

class TimeLogSystem : IntervalIteratingSystem(oneOf(ImmersionTimerComponent::class, IRLTimeComponent::class).get(), 1/60f) {

    override fun processEntity(entity: Entity) {

        val immersionTimerComponent = ImmersionTimerComponent.getFor(entity)
        val irlTimeComponent = IRLTimeComponent.getFor(entity)

        if (irlTimeComponent != null) {
            MessageChannelEnum.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessage.LogViewMessageType.LocalTime, irlTimeComponent.localTime()))
        }

        if ( immersionTimerComponent != null ) {
            MessageChannelEnum.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessage.LogViewMessageType.InstImmersionTime, immersionTimerComponent.instImmersionTime()) )
            MessageChannelEnum.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessage.LogViewMessageType.CumlImmersionTime, immersionTimerComponent.cumlImmersionTime()) )
        }
    }
}