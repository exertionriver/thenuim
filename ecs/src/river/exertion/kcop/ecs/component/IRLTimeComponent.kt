package river.exertion.kcop.ecs.component

import river.exertion.kcop.base.Id
import river.exertion.kcop.ecs.EngineHandler
import java.time.LocalDateTime
import java.util.*

@Suppress("NewApi")
class IRLTimeComponent : IComponent {

    override var componentId = Id.randomId()
    override var isInitialized = false

    private fun irlTime() = Calendar.getInstance()

    private fun localDateTime() = LocalDateTime.of(irlTime().get(Calendar.YEAR), irlTime().get(Calendar.MONTH) + 1, irlTime().get(Calendar.DAY_OF_MONTH),
        irlTime().get(Calendar.HOUR_OF_DAY), irlTime().get(Calendar.MINUTE), irlTime().get(Calendar.SECOND))

    private fun localHoursStr() = localDateTime().hour.toString().padStart(2, '0')
    private fun localMinutesStr() = localDateTime().minute.toString().padStart(2, '0')
    private fun localSecondsStr() = localDateTime().second.toString().padStart(2, '0')

    fun localTime() = "${localHoursStr()}:${localMinutesStr()}:${localSecondsStr()}"

    companion object : IComponentCompanion {
        override fun ecsInit(entityName : String, initData : Any?) {
            EngineHandler.replaceComponent<IRLTimeComponent>(entityName, initData)
        }
    }
}