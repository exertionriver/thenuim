package river.exertion.kcop.automation.btree.behavior

import ktx.ai.*
import river.exertion.kcop.automation.AutoUser
import river.exertion.kcop.automation.btree.AutoUserBehavior
import river.exertion.kcop.automation.btree.task.control.BehaviorCompleteControlTask
import river.exertion.kcop.automation.btree.task.view.ButtonViewClickTask
import river.exertion.kcop.automation.btree.task.view.KcopButtonClickTask
import river.exertion.kcop.automation.btree.task.core.WaitTask
import java.util.*

class ClickDisplayModeButtonBehavior : AutoUserBehavior() {

    override val id = UUID.fromString("f1bcbbe2-f125-4287-a200-1b7a00cf9965")

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