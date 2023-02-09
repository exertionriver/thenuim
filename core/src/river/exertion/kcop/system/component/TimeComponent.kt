package river.exertion.kcop.system.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.TimeUtils
import ktx.ashley.mapperFor
import java.time.LocalDateTime
import java.util.Calendar

@Suppress("NewApi")
class TimeComponent(val startTime : Long = TimeUtils.millis()) : Component {

    fun irlTime() = Calendar.getInstance()

    val activeTime = startTime
    var renderTime = 0f

    fun activeTime() = TimeUtils.timeSinceMillis(activeTime) / 1000f

    private fun immersionTimeHours() = TimeUtils.timeSinceMillis(startTime) / (1000f * 60f * 60f)
    private fun immersionTimeMinutes() = TimeUtils.timeSinceMillis(startTime) / (1000f * 60f)
    private fun immersionTimeSeconds() = TimeUtils.timeSinceMillis(startTime) / 1000f

    private fun immersionTimeHoursStr() = immersionTimeHours().toInt().toString().padStart(2, '0')
    private fun immersionTimeMinutesStr() = immersionTimeMinutes().toInt().toString().padStart(2, '0')
    private fun immersionTimeSecondsStr() = immersionTimeSeconds().toInt().toString().padStart(2, '0')

    fun immersionTime() = "${immersionTimeHoursStr()}:${immersionTimeMinutesStr()}:${immersionTimeSecondsStr()}"

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