package river.exertion.kcop.view.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import ktx.ashley.oneOf
import river.exertion.kcop.ecs.component.IRLTimeComponent
import river.exertion.kcop.ecs.component.ImmersionTimerComponent
import river.exertion.kcop.ecs.immersionTimer.ImmersionTimer
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.view.messaging.LogViewMessage
import river.exertion.kcop.view.messaging.LogViewMessage.Companion.LogViewBridge

class TimeLogSystem : IntervalIteratingSystem(oneOf(ImmersionTimerComponent::class, IRLTimeComponent::class).get(), 1/60f) {

    override fun processEntity(entity: Entity) {

        val immersionTimerComponent = ImmersionTimerComponent.getFor(entity)
        val irlTimeComponent = IRLTimeComponent.getFor(entity)

        if (irlTimeComponent != null) {
            MessageChannelHandler.send(LogViewBridge, LogViewMessage(LogViewMessage.LogViewMessageType.LocalTime, irlTimeComponent.localTime()))
        } else {
            MessageChannelHandler.send(LogViewBridge, LogViewMessage(LogViewMessage.LogViewMessageType.LocalTime, ImmersionTimer.CumlTimeZero))
        }

        if ( immersionTimerComponent != null ) {
            MessageChannelHandler.send(LogViewBridge, LogViewMessage(LogViewMessage.LogViewMessageType.InstImmersionTime, immersionTimerComponent.instImmersionTime()) )
            MessageChannelHandler.send(LogViewBridge, LogViewMessage(LogViewMessage.LogViewMessageType.CumlImmersionTime, immersionTimerComponent.cumlImmersionTime()) )
        } else {
            MessageChannelHandler.send(LogViewBridge, LogViewMessage(LogViewMessage.LogViewMessageType.InstImmersionTime, ImmersionTimer.CumlTimeZero) )
            MessageChannelHandler.send(LogViewBridge, LogViewMessage(LogViewMessage.LogViewMessageType.CumlImmersionTime, ImmersionTimer.CumlTimeZero) )
        }
    }
}