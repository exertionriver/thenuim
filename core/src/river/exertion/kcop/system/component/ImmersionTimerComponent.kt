package river.exertion.kcop.system.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.TimeUtils
import ktx.ashley.mapperFor
import river.exertion.kcop.system.immersionTimer.ImmersionTimer
import river.exertion.kcop.system.immersionTimer.ImmersionTimerState

class ImmersionTimerComponent(startTime : Long = TimeUtils.millis(), startState : ImmersionTimerState = ImmersionTimerState.PAUSED) : Component {

    val instImmersionTimer = ImmersionTimer(startTime, startState)
    val cumlImmersionTimer = ImmersionTimer(startTime, startState)

    companion object {
        val mapper = mapperFor<ImmersionTimerComponent>()

        fun has(entity : Entity) : Boolean = entity.components.firstOrNull{ it is ImmersionTimerComponent } != null
        fun getFor(entity : Entity) : ImmersionTimerComponent? = if (has(entity)) entity.components.first { it is ImmersionTimerComponent } as ImmersionTimerComponent else null

    }
}