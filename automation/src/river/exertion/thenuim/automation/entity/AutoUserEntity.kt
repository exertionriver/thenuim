package river.exertion.thenuim.automation.entity

import com.badlogic.ashley.core.Component
import river.exertion.thenuim.automation.component.AutoUserComponent
import river.exertion.thenuim.ecs.entity.IEntity

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