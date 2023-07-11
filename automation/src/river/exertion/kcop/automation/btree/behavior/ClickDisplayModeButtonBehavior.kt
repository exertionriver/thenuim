package river.exertion.kcop.automation.btree.behavior

import ktx.ai.*
import river.exertion.kcop.automation.btree.AutoUser
import river.exertion.kcop.automation.btree.AutoUserBehavior
import river.exertion.kcop.automation.btree.task.BehaviorCompleteControlTask
import river.exertion.kcop.automation.btree.task.ButtonOneClickTask
import river.exertion.kcop.automation.btree.task.WaitTask

class ClickDisplayModeButtonBehavior : AutoUserBehavior() {

    override val name: String = "clickBehavior"

    override fun btree(autoUser: AutoUser) =
        behaviorTree(blackboard = autoUser) {
            sequence {
                add(ButtonOneClickTask())
                add(WaitTask(2f))
                add(ButtonOneClickTask())
                add(BehaviorCompleteControlTask())
            }
        }
}