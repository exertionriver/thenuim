package river.exertion.kcop.system.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import com.badlogic.gdx.ai.GdxAI
import ktx.ashley.allOf
import river.exertion.kcop.system.SystemManager
import river.exertion.kcop.system.component.TimeComponent

class TimeSystem : IntervalIteratingSystem(allOf(TimeComponent::class).get(), 1/60f) {

    override fun processEntity(entity: Entity) {

        GdxAI.getTimepiece().update(this.interval)
        TimeComponent.getFor(entity)!!.timeRender += this.interval

        SystemManager.logDebug(this.javaClass.name, "${this.interval} passed, timeActive: ${TimeComponent.getFor(entity)!!.timeActive()}, timeRender: ${TimeComponent.getFor(entity)!!.timeRender()}")
    }
}