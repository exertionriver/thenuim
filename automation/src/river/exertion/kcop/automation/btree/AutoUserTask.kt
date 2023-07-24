package river.exertion.kcop.automation.btree

import com.badlogic.gdx.ai.btree.LeafTask
import com.badlogic.gdx.ai.btree.Task
import river.exertion.kcop.automation.AutoUser
import river.exertion.kcop.automation.AutoUserTestHandler
import river.exertion.kcop.automation.btree.task.control.BehaviorCompleteControlTask
import river.exertion.kcop.automation.btree.task.core.NoopTask
import river.exertion.kcop.automation.btree.task.core.WaitTask
import river.exertion.kcop.base.Log
import java.util.UUID

abstract class AutoUserTask : LeafTask<AutoUser>() {

    abstract val id : UUID

    val name : String = this::class.simpleName!!

    override fun copyTo(task: Task<AutoUser>?): Task<AutoUser> {
        TODO("Not yet implemented")
    }

    //executes in context of task queue
    abstract fun executeTask()

    //executes in context of btree
    override fun execute(): Status {
        AutoUserTestHandler.currentBehavior.readyTasks.add(this)
        Log.debug("loading ${AutoUserTestHandler.currentBehavior.name}", this.name)
        return returnStatus()
    }

    open fun returnStatus() = Status.SUCCEEDED

    fun allowScreenshot() =
        ( (name != WaitTask::class.simpleName)
            && (name != NoopTask::class.simpleName)
            && (name != BehaviorCompleteControlTask::class.simpleName)
        )
}
