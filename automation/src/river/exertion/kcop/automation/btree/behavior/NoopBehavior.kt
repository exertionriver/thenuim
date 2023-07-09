package river.exertion.kcop.automation.btree.behavior

import ktx.ai.*
import river.exertion.kcop.automation.btree.AutoUser
import river.exertion.kcop.automation.btree.IUserBehavior
import river.exertion.kcop.automation.btree.task.NoopTask

class NoopBehavior(param : Int) : IUserBehavior {

    override val name: String = "noopBehavior$param"

    override fun btree(autoUser: AutoUser) =
        behaviorTree(blackboard = autoUser) {
        sequence {
            add(NoopTask(1))
            add(NoopTask(2))
            add(NoopTask(3))
        }
    }
}