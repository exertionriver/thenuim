package river.exertion.kcop.system.entity

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity

interface IEntity : Component {

    var entityName : String
    var entity : Entity?

    var isInitialized : Boolean

    fun initialize(entity: Entity, initData: Any?) {
        this.entity = entity

        components.forEach {
            if (!entity.components.contains(it) ) entity.add(it)
        }

        isInitialized = true
    }

    //overall narrative timeline
    var components : MutableList<Component>
}