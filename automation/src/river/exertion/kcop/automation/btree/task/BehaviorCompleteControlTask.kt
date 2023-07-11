package river.exertion.kcop.automation.btree.task

import river.exertion.kcop.automation.btree.AutoUserBehaviorHandler
import river.exertion.kcop.automation.btree.AutoUserTask
import river.exertion.kcop.base.Log

class BehaviorCompleteControlTask : AutoUserTask() {

    override fun executeTask() {
        Log.debug("behavior complete.")
    }

    override fun execute() : Status {
        AutoUserBehaviorHandler.currentBehavior.behaviorComplete = true

        return super.execute()
    }
}
