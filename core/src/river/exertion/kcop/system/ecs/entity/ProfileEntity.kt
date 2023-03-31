package river.exertion.kcop.system.ecs.entity

import com.badlogic.ashley.core.Component
import river.exertion.kcop.system.ecs.component.IRLTimeComponent

class ProfileEntity : IEntity {

    override var entityName = Companion.entityName
    override var isInitialized = false

    override var components : MutableList<Component> = mutableListOf(
        IRLTimeComponent()
    )

    companion object {
        const val entityName = "immersionProfileEntity"
    }
}