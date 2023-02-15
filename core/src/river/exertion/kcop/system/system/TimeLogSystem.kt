package river.exertion.kcop.system.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import ktx.ashley.oneOf
import river.exertion.kcop.system.MessageChannel
import river.exertion.kcop.system.component.IRLTimeComponent
import river.exertion.kcop.system.component.ImmersionTimerComponent
import river.exertion.kcop.system.component.NarrativeComponent
import river.exertion.kcop.system.view.LogViewMessage
import river.exertion.kcop.system.view.LogViewMessageType

class TimeLogSystem : IntervalIteratingSystem(oneOf(ImmersionTimerComponent::class, IRLTimeComponent::class, NarrativeComponent::class).get(), 1/10f) {

    override fun processEntity(entity: Entity) {

        val immersionTimerComponent = ImmersionTimerComponent.getFor(entity)
        val irlTimeComponent = IRLTimeComponent.getFor(entity)
        val narrativeTimeComponent = NarrativeComponent.getFor(entity)?.narrativeImmersionTimer

        if (irlTimeComponent != null) {
            MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.LocalTime, irlTimeComponent.localTime()))
        }

        if (immersionTimerComponent != null) {
            MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.InstImmersionTime, immersionTimerComponent.instImmersionTimer.immersionTime(), immersionTimerComponent.instImmersionTimer.id) )
            MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.CumlImmersionTime, immersionTimerComponent.cumlImmersionTimer.immersionTime(), immersionTimerComponent.cumlImmersionTimer.id) )
        }

        if (narrativeTimeComponent != null) {
            MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.InstImmersionTime, narrativeTimeComponent.instImmersionTimer.immersionTime(), narrativeTimeComponent.instImmersionTimer.id) )
            MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(LogViewMessageType.CumlImmersionTime, narrativeTimeComponent.cumlImmersionTimer.immersionTime(), narrativeTimeComponent.cumlImmersionTimer.id) )
        }

//        SystemManager.logDebug(this.javaClass.name, "${this.interval} passed, timeActive: ${TimeComponent.getFor(entity)!!.timeActive()}, timeRender: ${TimeComponent.getFor(entity)!!.timeRender()}")
//        SystemManager.logDebug(this.javaClass.name, "instImmersionTime: ${immersionTimeComponent.instImmersionTime()}, cumlImmersionTime: ${immersionTimeComponent.cumlImmersionTime()}, localTime:${immersionTimeComponent.localTime()}" )
    }
}