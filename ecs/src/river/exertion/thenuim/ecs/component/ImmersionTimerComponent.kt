package river.exertion.thenuim.ecs.component

import river.exertion.thenuim.asset.immersionTimer.ImmersionTimer
import river.exertion.thenuim.asset.immersionTimer.ImmersionTimerPair
import river.exertion.thenuim.base.Id

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
}