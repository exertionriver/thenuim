package river.exertion.kcop.system.entity

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.mapperFor
import ktx.ashley.with
import ktx.assets.Asset
import river.exertion.kcop.Id
import river.exertion.kcop.assets.NarrativeAsset
import river.exertion.kcop.narrative.structure.Narrative
import river.exertion.kcop.system.MessageChannel
import river.exertion.kcop.system.SystemManager
import river.exertion.kcop.system.component.ImmersionTimerComponent
import river.exertion.kcop.system.component.NarrativeComponent
import river.exertion.kcop.system.immersionTimer.ImmersionTimerState
import river.exertion.kcop.system.view.LogViewMessage
import river.exertion.kcop.system.view.LogViewMessageType

class NarrativeEntity : Component, Id()  {

    var entityName = "narrative"
    lateinit var entity : Entity

    var isInitialized = false

    fun initialize(entity: Entity, narrative : Narrative?, initName : String = entityName) {
        this.entity = entity
        this.entityName = initName

        components.forEach {
            if (!entity.components.contains(it) ) entity.add(it)
        }

        NarrativeComponent.getFor(entity)!!.narrative = narrative
        NarrativeComponent.getFor(entity)!!.narrative?.init()
        NarrativeComponent.getFor(entity)!!.initTimers()

        isInitialized = true
    }

    //overall narrative timeline
    var components = mutableListOf(
        NarrativeComponent()
    )

    companion object {
        val mapper = mapperFor<NarrativeEntity>()

        fun has(entity : Entity) : Boolean { return entity.components.firstOrNull{ it is NarrativeEntity } != null }
        fun getFor(entity : Entity) : NarrativeEntity? = if (has(entity)) entity.components.first { it is NarrativeEntity } as NarrativeEntity else null

        fun instantiate(engine: PooledEngine, narrativeAsset : NarrativeAsset) : Entity {
            val newNarrative = engine.entity {
                with<NarrativeEntity>()
            }.apply { this[mapper]?.initialize(this, narrativeAsset.narrative) }

            SystemManager.logDebug (::instantiate.javaClass.name, "${getFor(newNarrative)!!.entityName} instantiated!")
            return newNarrative
        }
    }
}