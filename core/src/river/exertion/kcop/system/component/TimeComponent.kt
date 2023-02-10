package river.exertion.kcop.system.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.TimeUtils
import ktx.ashley.mapperFor
import java.time.LocalDateTime
import java.util.*

@Suppress("NewApi")
class TimeComponent(startTime : Long = TimeUtils.millis()) : Component {

    fun irlTime() = Calendar.getInstance()

    var renderTime = 0f

    private val immersionTime = ImmersionTime(startTime)

    fun immersionTime() = immersionTime.immersionTime()

    private fun localDateTime() = LocalDateTime.of(irlTime().get(Calendar.YEAR), irlTime().get(Calendar.MONTH), irlTime().get(Calendar.DAY_OF_MONTH),
        irlTime().get(Calendar.HOUR_OF_DAY), irlTime().get(Calendar.MINUTE), irlTime().get(Calendar.SECOND))

    private fun localHoursStr() = localDateTime().hour.toString().padStart(2, '0')
    private fun localMinutesStr() = localDateTime().minute.toString().padStart(2, '0')
    private fun localSecondsStr() = localDateTime().second.toString().padStart(2, '0')

    fun localTime() = "${localHoursStr()}:${localMinutesStr()}:${localSecondsStr()}"

    companion object {
        val mapper = mapperFor<TimeComponent>()

        fun has(entity : Entity) : Boolean = entity.components.firstOrNull{ it is TimeComponent } != null
        fun getFor(entity : Entity) : TimeComponent? = if (has(entity)) entity.components.first { it is TimeComponent } as TimeComponent else null

    }
}