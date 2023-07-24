package river.exertion.kcop.automation

import river.exertion.kcop.automation.btree.AutoUserBehavior

class AutoUserTest {

    val testBehaviors = mutableListOf<AutoUserBehavior>()
    val logBehaviors = mutableMapOf<AutoUserBehavior, TestResult>()

    enum class TestResult {
        PASSED, FAILED, READY, COMPLETED
    }
}