package river.exertion.kcop.ecs.component

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.TimeUtils
import river.exertion.kcop.ecs.ECSPackage
import river.exertion.kcop.ecs.entity.SubjectEntity
import river.exertion.kcop.ecs.messaging.EngineComponentMessage
import river.exertion.kcop.plugin.immersionTimer.ImmersionTimer
import river.exertion.kcop.plugin.immersionTimer.ImmersionTimerPair
import river.exertion.kcop.plugin.immersionTimer.ImmersionTimerState
import river.exertion.kcop.messaging.Id
import river.exertion.kcop.messaging.MessageChannelHandler

class ImmersionTimerComponent(startTime : Long = TimeUtils.millis(), startState : ImmersionTimerState = ImmersionTimerState.PAUSED) :
    IComponent {

    //ImmersionTimerComponent allows TimeLogSystem to easily update log view for active timers
    val id = Id.randomId()
    override fun componentId() = id

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
            MessageChannelHandler.send(
                ECSPackage.EngineComponentBridge, EngineComponentMessage(
                EngineComponentMessage.EngineComponentMessageType.ReplaceComponent,
                SubjectEntity.entityName, ImmersionTimerComponent::class.java, immersionTimerPair)
            )
        }
    }
}