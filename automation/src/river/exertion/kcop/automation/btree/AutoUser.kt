package river.exertion.kcop.automation.btree

import river.exertion.kcop.base.Log

class AutoUser {

    val name = "autoUser"

    lateinit var currentBehavior : IUserBehavior

    val behaviorSequenceList = mutableListOf<IUserBehavior>()
    val behaviorLogList = mutableListOf<IUserBehavior>()

    fun exec() {
        if (behaviorSequenceList.isNotEmpty()) {
            currentBehavior = behaviorSequenceList.first()
            val execTree = currentBehavior.btree(this)
            repeat(3) {
                execTree.step()
            }
            behaviorLogList.add(currentBehavior)
            behaviorSequenceList.removeFirst()
        } else {
            Log.debug("AutoUser::exec()", "no sequenced behaviors left")
        }
    }
}