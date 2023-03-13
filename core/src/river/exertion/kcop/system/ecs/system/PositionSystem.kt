package river.exertion.kcop.system.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import ktx.ashley.allOf
import river.exertion.kcop.system.ecs.SystemManager
import river.exertion.kcop.system.ecs.component.PositionComponent

class PositionSystem : IntervalIteratingSystem(allOf(PositionComponent::class).get(), 1/60f) {

    override fun processEntity(entity: Entity) {

        SystemManager.logDebug(this.javaClass.name, "${this.interval} passed, positionActive: ${PositionComponent.getFor(entity)!!.positionActive()}}")
    }
}