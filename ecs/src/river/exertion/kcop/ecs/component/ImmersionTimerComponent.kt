package river.exertion.kcop.ecs.component

import river.exertion.kcop.asset.immersionTimer.ImmersionTimer
import river.exertion.kcop.asset.immersionTimer.ImmersionTimerPair
import river.exertion.kcop.base.Id
import river.exertion.kcop.ecs.EngineHandler
import river.exertion.kcop.ecs.entity.SubjectEntity

class ImmersionTimerComponent : IComponent {

    //ImmersionTimerComponent allows TimeLogSystem to easily update log view for active timers
    override var componentId = Id.randomId()
    override var isInitialized = false

    private var immersionTimerPair = ImmersionTimerPair(
        ImmersionTimer(),
        ImmersionTimer()
    )

    override fun initialize(initData: Any?) {
        val initTimer = IComponent.checkInitType<ImmersionTimerPair>(initData)

        if (initTimer != null) {
            immersionTimerPair = initTimer

            super.initialize(initData)
        }
    }

    fun instImmersionTime() = if (isInitialized) immersionTimerPair.instImmersionTimer.immersionTime() else ImmersionTimer.CumlTimeZero
    fun cumlImmersionTime() = if (isInitialized) immersionTimerPair.cumlImmersionTimer.immersionTime() else ImmersionTimer.CumlTimeZero

    companion object {
        fun ecsInit(entityName : String = SubjectEntity.entityName, immersionTimerPair : ImmersionTimerPair? = null) {
            EngineHandler.replaceComponent<ImmersionTimerComponent>(entityName, immersionTimerPair)
        }
    }
}