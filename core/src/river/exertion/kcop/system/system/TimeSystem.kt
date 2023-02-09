package river.exertion.kcop.system.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import com.badlogic.gdx.ai.GdxAI
import ktx.ashley.allOf
import river.exertion.kcop.system.MessageChannel
import river.exertion.kcop.system.SystemManager
import river.exertion.kcop.system.component.TimeComponent
import river.exertion.kcop.system.view.LogViewMessage
import river.exertion.kcop.system.view.LogViewMessageType
import river.exertion.kcop.system.view.ViewType

class TimeSystem : IntervalIteratingSystem(allOf(TimeComponent::class).get(), 1f) {

    override fun processEntity(entity: Entity) {

        GdxAI.getTimepiece().update(this.interval)
        TimeComponent.getFor(entity)!!.renderTime += this.interval
        val timeComponent = TimeComponent.getFor(entity)!!

        MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(ViewType.LOG, LogViewMessageType.LocalTime, timeComponent.localTime()))
        MessageChannel.LOG_VIEW_BRIDGE.send(null, LogViewMessage(ViewType.LOG, LogViewMessageType.ImmersionTime, timeComponent.immersionTime()))

//        SystemManager.logDebug(this.javaClass.name, "${this.interval} passed, timeActive: ${TimeComponent.getFor(entity)!!.timeActive()}, timeRender: ${TimeComponent.getFor(entity)!!.timeRender()}")
        SystemManager.logDebug(this.javaClass.name, "immersionTime: ${timeComponent.immersionTime()}, localTime:${timeComponent.localTime()}" )
    }
}