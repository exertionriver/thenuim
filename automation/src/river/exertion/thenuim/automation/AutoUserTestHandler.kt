package river.exertion.thenuim.automation

import river.exertion.thenuim.automation.btree.AutoUserBehavior
import river.exertion.thenuim.automation.btree.behavior.NoopBehavior

object AutoUserTestHandler {

    var currentTest = river.exertion.thenuim.automation.AutoUserTest()
    var currentBehavior : AutoUserBehavior = NoopBehavior()

    private val autoUser = river.exertion.thenuim.automation.AutoUser()

    //step through btree until completion
    fun execTestBehaviors() {
        if (river.exertion.thenuim.automation.AutoUserTestHandler.currentTest.testBehaviors.isNotEmpty()) {
            river.exertion.thenuim.automation.AutoUserTestHandler.currentBehavior = river.exertion.thenuim.automation.AutoUserTestHandler.currentTest.testBehaviors.first()

            while (!river.exertion.thenuim.automation.AutoUserTestHandler.currentBehavior.behaviorComplete) {
                river.exertion.thenuim.automation.AutoUserTestHandler.currentBehavior.btree(river.exertion.thenuim.automation.AutoUserTestHandler.autoUser).step()
            }

            river.exertion.thenuim.automation.AutoUserTestHandler.currentTest.logBehaviors[river.exertion.thenuim.automation.AutoUserTestHandler.currentBehavior] =
                river.exertion.thenuim.automation.AutoUserTest.TestResult.READY
            river.exertion.thenuim.automation.AutoUserTestHandler.currentTest.testBehaviors.removeFirst()
        } else {
        //    Log.debug("AutoUserBehaviorHandler::execBehavior()", "no sequenced behaviors left")
        }
    }
}


