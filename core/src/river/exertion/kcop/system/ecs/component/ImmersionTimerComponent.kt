package river.exertion.kcop.system.ecs.component

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.TimeUtils
import river.exertion.kcop.system.immersionTimer.ImmersionTimer
import river.exertion.kcop.system.immersionTimer.ImmersionTimerPair
import river.exertion.kcop.system.immersionTimer.ImmersionTimerState

class ImmersionTimerComponent(startTime : Long = TimeUtils.millis(), startState : ImmersionTimerState = ImmersionTimerState.PAUSED) : IComponent {

    //ImmersionTimerComponent allows TimeLogSystem to easily update log view for active timers

    var immersionTimerPair = ImmersionTimerPair(
        ImmersionTimer(startTime, startState),
        ImmersionTimer(startTime, startState)
    )

    override var isInitialized = false

    override fun initialize(initData: Any?) {
        val initTimer = IComponent.checkInitType<ImmersionTimerPair>(initData)

        if (initTimer != null) {

            immersionTimerPair = initTimer

            super.initialize(initData)
        }
    }

    fun instImmersionTime() = if (isInitialized) immersionTimerPair.instImmersionTimer!!.immersionTime() else ImmersionTimer.zero()
    fun cumlImmersionTime() = if (isInitialized) immersionTimerPair.cumlImmersionTimer.immersionTime() else ImmersionTimer.zero()

    companion object {
        fun has(entity : Entity) : Boolean = entity.components.firstOrNull{ it is ImmersionTimerComponent } != null
        fun getFor(entity : Entity) : ImmersionTimerComponent? = if (has(entity)) entity.components.first { it is ImmersionTimerComponent } as ImmersionTimerComponent else null
    }
}