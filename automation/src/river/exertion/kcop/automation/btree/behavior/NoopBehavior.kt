package river.exertion.kcop.automation.btree.behavior

import ktx.ai.add
import ktx.ai.behaviorTree
import ktx.ai.sequence
import river.exertion.kcop.automation.btree.AutoUser
import river.exertion.kcop.automation.btree.AutoUserBehavior
import river.exertion.kcop.automation.btree.task.NoopTask

class NoopBehavior(param : Int) : AutoUserBehavior() {

    override val name: String = "noopBehavior$param"

    override fun btree(autoUser: AutoUser) =
        behaviorTree(blackboard = autoUser) {
        sequence {
            add(NoopTask())
        }
    }
}