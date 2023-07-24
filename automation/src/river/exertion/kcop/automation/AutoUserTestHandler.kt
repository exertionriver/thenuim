package river.exertion.kcop.automation

import river.exertion.kcop.automation.btree.AutoUserBehavior
import river.exertion.kcop.automation.btree.behavior.NoopBehavior

object AutoUserTestHandler {

    var currentTest = AutoUserTest()
    var currentBehavior : AutoUserBehavior = NoopBehavior()

    private val autoUser = AutoUser()

    //step through btree until completion
    fun execTestBehaviors() {
        if (currentTest.testBehaviors.isNotEmpty()) {
            currentBehavior = currentTest.testBehaviors.first()

            while (!currentBehavior.behaviorComplete) {
                currentBehavior.btree(autoUser).step()
            }

            currentTest.logBehaviors[currentBehavior] = AutoUserTest.TestResult.READY
            currentTest.testBehaviors.removeFirst()
        } else {
        //    Log.debug("AutoUserBehaviorHandler::execBehavior()", "no sequenced behaviors left")
        }
    }
}


