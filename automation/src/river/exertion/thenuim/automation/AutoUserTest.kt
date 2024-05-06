package river.exertion.thenuim.automation

import river.exertion.thenuim.automation.btree.AutoUserBehavior

class AutoUserTest {

    val testBehaviors = mutableListOf<AutoUserBehavior>()
    val logBehaviors = mutableMapOf<AutoUserBehavior, river.exertion.thenuim.automation.AutoUserTest.TestResult>()

    enum class TestResult {
        PASSED, FAILED, READY, COMPLETED
    }
}