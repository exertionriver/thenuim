package river.exertion.kcop.automation.entity

import com.badlogic.ashley.core.Component
import river.exertion.kcop.automation.component.AutoUserComponent
import river.exertion.kcop.ecs.entity.IEntity

class AutoUserEntity : IEntity {

    override var entityName = Companion.entityName
    override var isInitialized = false

    override var components : MutableList<Component> = mutableListOf(
        AutoUserComponent()
    )

    companion object {
        const val entityName = "autoUserEntity"
    }
}