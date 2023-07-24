package river.exertion.kcop.automation.btree

import com.badlogic.gdx.ai.btree.BehaviorTree
import river.exertion.kcop.automation.AutoUser
import river.exertion.kcop.automation.AutoUserTest
import java.util.*

abstract class AutoUserBehavior {

    abstract val id : UUID

    val name : String = this::class.simpleName!!

    var behaviorComplete : Boolean = false

    val readyTasks = mutableListOf<AutoUserTask>()
    val completedTasks = mutableMapOf<AutoUserTask, AutoUserTest.TestResult>()

    abstract fun btree(autoUser : AutoUser) : BehaviorTree<AutoUser>

    fun getResults(): AutoUserTest.TestResult {
        return when {
            completedTasks.values.count() == completedTasks.values.count { it == AutoUserTest.TestResult.PASSED } -> AutoUserTest.TestResult.PASSED
            completedTasks.values.count { it == AutoUserTest.TestResult.FAILED } > 0 -> AutoUserTest.TestResult.FAILED
            else -> AutoUserTest.TestResult.COMPLETED
        }
    }
}