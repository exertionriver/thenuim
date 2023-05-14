package river.exertion.kcop.ecs.switchboard

import river.exertion.kcop.ecs.ECSPackage.EngineComponentBridge
import river.exertion.kcop.ecs.component.ImmersionTimerComponent
import river.exertion.kcop.ecs.entity.SubjectEntity
import river.exertion.kcop.ecs.messaging.EngineComponentMessage
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.messaging.Switchboard
import river.exertion.kcop.messaging.SwitchboardEntry
import river.exertion.kcop.plugin.immersionTimer.ImmersionTimerPair

object ImmersionTimerSwitchboard {

    val ShowImmersionTimer = SwitchboardEntry("ShowImmersionTimer")

    fun addShowImmersionTimerStub() {
        Switchboard.addEntry(ShowImmersionTimer.apply { this.switchboardTagAction = { } } )
    }

    fun showImmersionTimer(timerPair: ImmersionTimerPair) {
        Switchboard.modifyAction(ShowImmersionTimer.switchboardTag) {
            MessageChannelHandler.send(
                    EngineComponentBridge, EngineComponentMessage(
                    EngineComponentMessage.EngineComponentMessageType.ReplaceComponent,
                    SubjectEntity.entityName, ImmersionTimerComponent::class.java, timerPair)
            )
        }

        Switchboard.executeAction(ShowImmersionTimer.switchboardTag)
    }
}

