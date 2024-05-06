package river.exertion.thenuim.automation.btree.task.control

import river.exertion.thenuim.automation.btree.AutoUserTask
import river.exertion.thenuim.base.Log
import java.util.*

class BehaviorCompleteControlTask : AutoUserTask() {

    override val id = UUID.fromString("16f8cc43-c920-411f-8345-b03bb4b77e42")

    override fun executeTask() {
        Log.debug("behavior complete.")
    }

    override fun execute() : Status {
        river.exertion.thenuim.automation.AutoUserTestHandler.currentBehavior.behaviorComplete = true

        return super.execute()
    }
}
