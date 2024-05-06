package river.exertion.thenuim.view.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import ktx.ashley.oneOf
import river.exertion.thenuim.ecs.component.ImmersionTimerComponent
import river.exertion.thenuim.asset.immersionTimer.ImmersionTimer
import river.exertion.thenuim.ecs.EngineHandler
import river.exertion.thenuim.ecs.component.IrlTimeComponent
import river.exertion.thenuim.view.layout.LogView

class TimeLogSystem : IntervalIteratingSystem(oneOf(ImmersionTimerComponent::class, IrlTimeComponent::class).get(), 1/60f) {

    override fun processEntity(entity: Entity) {

        val immersionTimerComponent = EngineHandler.getComponentFor<ImmersionTimerComponent>(entity)
        val irlTimeComponent = EngineHandler.getComponentFor<IrlTimeComponent>(entity)

        if (irlTimeComponent != null) {
            LogView.localTimeStr = irlTimeComponent.localTime()
            LogView.rebuildTextTimeReadout()
        } else {
            LogView.localTimeStr = ImmersionTimer.CumlTimeZero
            LogView.rebuildTextTimeReadout()
        }

        if ( immersionTimerComponent != null ) {
            LogView.instImmersionTimeStr = immersionTimerComponent.instImmersionTime()
            LogView.cumlImmersionTimeStr = immersionTimerComponent.cumlImmersionTime()
            LogView.rebuildTextTimeReadout()
        } else {
            LogView.instImmersionTimeStr = ImmersionTimer.CumlTimeZero
            LogView.cumlImmersionTimeStr = ImmersionTimer.CumlTimeZero
            LogView.rebuildTextTimeReadout()
        }
    }
}