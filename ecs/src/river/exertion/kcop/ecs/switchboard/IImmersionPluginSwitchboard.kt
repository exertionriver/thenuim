package river.exertion.kcop.ecs.switchboard

import river.exertion.kcop.ecs.ECSPackage.EngineComponentBridge
import river.exertion.kcop.ecs.component.ImmersionTimerComponent
import river.exertion.kcop.ecs.entity.SubjectEntity
import river.exertion.kcop.ecs.messaging.EngineComponentMessage
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.messaging.Switchboard
import river.exertion.kcop.messaging.SwitchboardEntry
import river.exertion.kcop.plugin.IImmersionPackage

object IImmersionPluginSwitchboard {

    val ShowImmersionTimer = SwitchboardEntry("ShowImmersionTimer")
    val ShowCompletionStatus = SwitchboardEntry("ShowCompletionStatus")
    val HideCompletionStatus = SwitchboardEntry("HideCompletionStatus")

    fun IImmersionPackage.showImmersionTimer() {
        if (Switchboard.checkByTag(ShowImmersionTimer.switchboardTag) == null) {
            Switchboard.addEntry(ShowImmersionTimer.apply { this.switchboardTagAction = {
                MessageChannelHandler.send(
                    EngineComponentBridge, EngineComponentMessage(
                    EngineComponentMessage.EngineComponentMessageType.ReplaceComponent,
                    SubjectEntity.entityName, ImmersionTimerComponent::class.java, timerPair())
                )
            } } )
        } else {
            Switchboard.executeAction(ShowImmersionTimer.switchboardTag)
        }
    }
}