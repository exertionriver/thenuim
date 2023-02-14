package river.exertion.kcop.system.entity

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.mapperFor
import ktx.ashley.with
import river.exertion.kcop.Id
import river.exertion.kcop.system.SystemManager
import river.exertion.kcop.system.component.IRLTimeComponent
import river.exertion.kcop.system.component.ImmersionTimerComponent
import river.exertion.kcop.system.immersionTimer.ImmersionTimerState

class Observer : Component, Id() {

    var entityName = "observer"

    fun initialize(entity: Entity, initName : String = entityName) {
        entityName = initName

        components.forEach {
            if (!entity.components.contains(it) ) entity.add(it)
        }
    }

    var components = mutableListOf(
        IRLTimeComponent(),
        ImmersionTimerComponent(startState = ImmersionTimerState.RUNNING)
    )

    companion object {
        val mapper = mapperFor<Observer>()

        fun has(entity : Entity) : Boolean { return entity.components.firstOrNull{ it is Observer } != null }
        fun getFor(entity : Entity) : Observer? = if (has(entity)) entity.components.first { it is Observer } as Observer else null

        fun instantiate(engine: PooledEngine) : Entity {
            val newObserver = engine.entity {
                with<Observer>()
            }.apply { this[mapper]?.initialize(this) }

            SystemManager.logDebug (::instantiate.javaClass.name, "${getFor(newObserver)!!.entityName} instantiated! @ ${IRLTimeComponent.getFor(newObserver)!!.localTime()}")
            return newObserver
        }
    }
}