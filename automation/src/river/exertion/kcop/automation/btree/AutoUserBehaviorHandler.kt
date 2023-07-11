package river.exertion.kcop.automation.btree

import river.exertion.kcop.base.Log

object AutoUserBehaviorHandler {

    lateinit var currentBehavior : AutoUserBehavior

    val behaviorSequenceList = mutableListOf<AutoUserBehavior>()
    val behaviorLogList = mutableListOf<AutoUserBehavior>()

    val taskSequenceList = mutableListOf<AutoUserTask>()
    val taskLogList = mutableListOf<AutoUserTask>()

    val autoUser = AutoUser()

    fun execBehavior() {
        if (behaviorSequenceList.isNotEmpty()) {
            currentBehavior = behaviorSequenceList.first()

            while (!currentBehavior.behaviorComplete) {
                currentBehavior.btree(autoUser).step()
            }

            behaviorLogList.add(currentBehavior)
            behaviorSequenceList.removeFirst()
        } else {
            Log.debug("AutoUserBehaviorHandler::execBehavior()", "no sequenced behaviors left")
        }
    }
}

