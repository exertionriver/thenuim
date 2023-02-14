package river.exertion.kcop.system.entity

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.mapperFor
import ktx.ashley.with
import river.exertion.kcop.Id
import river.exertion.kcop.narrative.structure.Narrative
import river.exertion.kcop.system.SystemManager
import river.exertion.kcop.system.component.ImmersionTimerComponent
import river.exertion.kcop.system.component.NarrativeComponent
import river.exertion.kcop.system.immersionTimer.ImmersionTimerState

class NarrativeEntity : Component, Id()  {

    var entityName = "narrative"

    fun initialize(entity: Entity, initName : String = entityName) {
        entityName = initName

        components.forEach {
            if (!entity.components.contains(it) ) entity.add(it)
        }
    }

    //overall narrative timeline
    var components = mutableListOf(
        ImmersionTimerComponent(startState = ImmersionTimerState.RUNNING),
        NarrativeComponent()
    )

    companion object {
        val mapper = mapperFor<NarrativeEntity>()

        fun has(entity : Entity) : Boolean { return entity.components.firstOrNull{ it is NarrativeEntity } != null }
        fun getFor(entity : Entity) : NarrativeEntity? = if (has(entity)) entity.components.first { it is NarrativeEntity } as NarrativeEntity else null

        fun instantiate(engine: PooledEngine, narrative : Narrative) : Entity {
            val newNarrative = engine.entity {
                with<NarrativeEntity>()
            }.apply { this[mapper]?.initialize(this) }

            NarrativeComponent.getFor(newNarrative)!!.narrative = narrative
            NarrativeComponent.getFor(newNarrative)!!.initTimers()

            SystemManager.logDebug (::instantiate.javaClass.name, "${getFor(newNarrative)!!.entityName} instantiated! @ ${ImmersionTimerComponent.getFor(newNarrative)!!.instImmersionTimer.immersionTime()}")
            return newNarrative
        }
    }
}