package river.exertion.kcop.ecs.entity

import com.badlogic.ashley.core.Component
import river.exertion.kcop.ecs.component.IrlTimeComponent

class SubjectEntity : IEntity {

    override var entityName = Companion.entityName
    override var isInitialized = false

    override var components : MutableList<Component> = mutableListOf(
        IrlTimeComponent()
    )

    companion object {
        const val entityName = "subjectEntity"
    }
}