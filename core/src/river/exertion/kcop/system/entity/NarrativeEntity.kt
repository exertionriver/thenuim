package river.exertion.kcop.system.entity

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import river.exertion.kcop.assets.NarrativeAsset
import river.exertion.kcop.system.component.NarrativeComponent

class NarrativeEntity : IEntity {

    override var entityName = "narrative"
    override var entity : Entity? = null

    override var isInitialized = false

    override fun initialize(entity: Entity, initData: Any?) {
        super.initialize(entity, initData)

        val narrativeAsset = if (initData != null) initData as NarrativeAsset else null
        val narrative = narrativeAsset?.narrative

        this.entityName = this.entityName + (narrative?.id ?: "")

        NarrativeComponent.getFor(entity)!!.narrative = narrative
        NarrativeComponent.getFor(entity)!!.narrative?.init()
        NarrativeComponent.getFor(entity)!!.initTimers()
    }

    //overall narrative timeline
    override var components: MutableList<Component> = mutableListOf(
        NarrativeComponent()
    )
}