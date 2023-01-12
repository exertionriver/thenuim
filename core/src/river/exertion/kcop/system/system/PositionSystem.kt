package river.exertion.kcop.system.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import ktx.ashley.allOf
import river.exertion.kcop.system.SystemManager
import river.exertion.kcop.system.component.PositionComponent

class PositionSystem : IntervalIteratingSystem(allOf(PositionComponent::class).get(), 1/60f) {

    override fun processEntity(entity: Entity) {

        SystemManager.logDebug(this.javaClass.name, "${this.interval} passed, positionActive: ${PositionComponent.getFor(entity)!!.positionActive()}}")
    }
}