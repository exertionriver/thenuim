package river.exertion.kcop.automation.btree.behavior

import ktx.ai.*
import river.exertion.kcop.automation.btree.AutoUser
import river.exertion.kcop.automation.btree.AutoUserBehavior
import river.exertion.kcop.automation.btree.task.BehaviorCompleteControlTask
import river.exertion.kcop.automation.btree.task.ButtonViewClickTask
import river.exertion.kcop.automation.btree.task.KcopButtonClickTask
import river.exertion.kcop.automation.btree.task.WaitTask

class ClickDisplayModeButtonBehavior : AutoUserBehavior() {

    override val name: String = "clickBehavior"

    override fun btree(autoUser: AutoUser) =
        behaviorTree(blackboard = autoUser) {
            sequence {
                (0..5).forEach {
                    add(ButtonViewClickTask(it))
                    add(WaitTask(.75f))
                    if (it == 3) {
                        add(KcopButtonClickTask())
                    }
                    else {
                        add(ButtonViewClickTask(it))
                    }
                    add(WaitTask(.75f))
                }
                add(BehaviorCompleteControlTask())
            }
        }
}