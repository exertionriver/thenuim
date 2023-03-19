package river.exertion.kcop.system.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.TimeUtils
import river.exertion.kcop.system.immersionTimer.ImmersionTimer
import river.exertion.kcop.system.immersionTimer.ImmersionTimerState

class ImmersionTimerComponent(startTime : Long = TimeUtils.millis(), startState : ImmersionTimerState = ImmersionTimerState.PAUSED) : IComponent {

    //ImmersionTimerComponent gathers inst and cuml timers and allows TimeLogSystem to easily update log view for active timers

    var instImmersionTimer = ImmersionTimer(startTime, startState)
    var cumlImmersionTimer = ImmersionTimer(startTime, startState)

    override var entityName = ""
    override var isInitialized = false

    companion object {
        fun has(entity : Entity) : Boolean = entity.components.firstOrNull{ it is ImmersionTimerComponent } != null
        fun getFor(entity : Entity) : ImmersionTimerComponent? = if (has(entity)) entity.components.first { it is ImmersionTimerComponent } as ImmersionTimerComponent else null
    }
}