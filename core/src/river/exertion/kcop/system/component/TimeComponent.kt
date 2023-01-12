package river.exertion.kcop.system.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.TimeUtils
import ktx.ashley.mapperFor

class TimeComponent(timeStart : Long = TimeUtils.millis()) : Component {

    var timeActive = timeStart
    var timeRender = 0f

    fun timeActive() = TimeUtils.timeSinceMillis(timeActive) / 1000f
    fun timeRender() = timeRender

    companion object {
        val mapper = mapperFor<TimeComponent>()

        fun has(entity : Entity) : Boolean = entity.components.firstOrNull{ it is TimeComponent } != null
        fun getFor(entity : Entity) : TimeComponent? = if (has(entity)) entity.components.first { it is TimeComponent } as TimeComponent else null

    }
}