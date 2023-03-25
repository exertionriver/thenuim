package river.exertion.kcop.system.ecs.component

import com.badlogic.ashley.core.Entity
import ktx.ashley.mapperFor
import java.time.LocalDateTime
import java.util.*

@Suppress("NewApi")
class IRLTimeComponent : IComponent {

    fun irlTime() = Calendar.getInstance()

    override var entityName = ""
    override var isInitialized = false

    private fun localDateTime() = LocalDateTime.of(irlTime().get(Calendar.YEAR), irlTime().get(Calendar.MONTH), irlTime().get(Calendar.DAY_OF_MONTH),
        irlTime().get(Calendar.HOUR_OF_DAY), irlTime().get(Calendar.MINUTE), irlTime().get(Calendar.SECOND))

    private fun localHoursStr() = localDateTime().hour.toString().padStart(2, '0')
    private fun localMinutesStr() = localDateTime().minute.toString().padStart(2, '0')
    private fun localSecondsStr() = localDateTime().second.toString().padStart(2, '0')

    fun localTime() = "${localHoursStr()}:${localMinutesStr()}:${localSecondsStr()}"

    companion object {
        fun has(entity : Entity) : Boolean = entity.components.firstOrNull{ it is IRLTimeComponent } != null
        fun getFor(entity : Entity) : IRLTimeComponent? = if (has(entity)) entity.components.first { it is IRLTimeComponent } as IRLTimeComponent else null
    }
}