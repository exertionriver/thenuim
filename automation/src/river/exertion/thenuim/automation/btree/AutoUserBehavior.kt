package river.exertion.thenuim.automation.btree

import com.badlogic.gdx.ai.btree.BehaviorTree
import river.exertion.thenuim.automation.AutoUser
import river.exertion.thenuim.automation.AutoUserTest
import java.util.*

abstract class AutoUserBehavior {

    abstract val id : UUID

    val name : String = this::class.simpleName!!

    var behaviorComplete : Boolean = false

    val readyTasks = mutableListOf<AutoUserTask>()
    val completedTasks = mutableMapOf<AutoUserTask, river.exertion.thenuim.automation.AutoUserTest.TestResult>()

    abstract fun btree(autoUser : river.exertion.thenuim.automation.AutoUser) : BehaviorTree<river.exertion.thenuim.automation.AutoUser>

    fun getResults(): river.exertion.thenuim.automation.AutoUserTest.TestResult {
        return when {
            completedTasks.values.count() == completedTasks.values.count { it == river.exertion.thenuim.automation.AutoUserTest.TestResult.PASSED } -> river.exertion.thenuim.automation.AutoUserTest.TestResult.PASSED
            completedTasks.values.count { it == river.exertion.thenuim.automation.AutoUserTest.TestResult.FAILED } > 0 -> river.exertion.thenuim.automation.AutoUserTest.TestResult.FAILED
            else -> river.exertion.thenuim.automation.AutoUserTest.TestResult.COMPLETED
        }
    }
}