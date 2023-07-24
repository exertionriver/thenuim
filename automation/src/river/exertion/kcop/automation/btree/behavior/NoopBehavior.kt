package river.exertion.kcop.automation.btree.behavior

import ktx.ai.add
import ktx.ai.behaviorTree
import ktx.ai.sequence
import river.exertion.kcop.automation.AutoUser
import river.exertion.kcop.automation.btree.AutoUserBehavior
import river.exertion.kcop.automation.btree.task.core.NoopTask
import java.util.*

class NoopBehavior : AutoUserBehavior() {

    override val id = UUID.fromString("682be1cc-c3b0-4a5c-8fc4-60f1e5a8ae9e")

    override fun btree(autoUser: AutoUser) =
        behaviorTree(blackboard = autoUser) {
        sequence {
            add(NoopTask())
        }
    }
}