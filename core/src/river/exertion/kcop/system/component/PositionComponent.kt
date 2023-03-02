package river.exertion.kcop.system.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector3
import ktx.ashley.mapperFor

class PositionComponent(positionStart : Vector3 = Vector3(0f, 0f, 0f)) : Component {

    var positionActive = positionStart
    fun positionActive() = positionActive

    companion object {
        fun has(entity : Entity) : Boolean = entity.components.firstOrNull{ it is PositionComponent } != null
        fun getFor(entity : Entity) : PositionComponent? = if (has(entity)) entity.components.first { it is PositionComponent } as PositionComponent else null
    }
}