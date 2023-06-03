package river.exertion.kcop.ecs.component

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.TimeUtils
import river.exertion.kcop.asset.Id
import river.exertion.kcop.asset.immersionTimer.ImmersionTimer
import river.exertion.kcop.asset.immersionTimer.ImmersionTimerPair
import river.exertion.kcop.asset.immersionTimer.ImmersionTimerState
import river.exertion.kcop.ecs.EngineHandler

class ImmersionTimerComponent(startTime : Long = TimeUtils.millis(), startState : ImmersionTimerState = ImmersionTimerState.PAUSED) :
    IComponent {

    //ImmersionTimerComponent allows TimeLogSystem to easily update log view for active timers
    override var componentId = Id.randomId()

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

    fun instImmersionTime() = if (isInitialized) immersionTimerPair.instImmersionTimer.immersionTime() else ImmersionTimer.CumlTimeZero
    fun cumlImmersionTime() = if (isInitialized) immersionTimerPair.cumlImmersionTimer.immersionTime() else ImmersionTimer.CumlTimeZero

    companion object {
        fun has(entity : Entity) : Boolean = entity.components.firstOrNull{ it is ImmersionTimerComponent } != null
        fun getFor(entity : Entity) : ImmersionTimerComponent? = if (has(entity)) entity.components.first { it is ImmersionTimerComponent } as ImmersionTimerComponent else null

        fun ecsInit(immersionTimerPair : ImmersionTimerPair) {
            EngineHandler.replaceComponent(componentClass = ImmersionTimerComponent::class.java, initInfo = immersionTimerPair)
        }
    }
}