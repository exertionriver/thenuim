package river.exertion.thenuim.automation.btree

import com.badlogic.gdx.ai.btree.LeafTask
import com.badlogic.gdx.ai.btree.Task
import river.exertion.thenuim.automation.btree.task.control.BehaviorCompleteControlTask
import river.exertion.thenuim.automation.btree.task.core.NoopTask
import river.exertion.thenuim.automation.btree.task.core.WaitTask
import river.exertion.thenuim.base.Log
import java.util.UUID

abstract class AutoUserTask : LeafTask<river.exertion.thenuim.automation.AutoUser>() {

    abstract val id : UUID

    val name : String = this::class.simpleName!!

    override fun copyTo(task: Task<river.exertion.thenuim.automation.AutoUser>?): Task<river.exertion.thenuim.automation.AutoUser> {
        TODO("Not yet implemented")
    }

    //executes in context of task queue
    abstract fun executeTask()

    //executes in context of btree
    override fun execute(): Status {
        river.exertion.thenuim.automation.AutoUserTestHandler.currentBehavior.readyTasks.add(this)
        Log.debug("loading ${river.exertion.thenuim.automation.AutoUserTestHandler.currentBehavior.name}", this.name)
        return returnStatus()
    }

    open fun returnStatus() = Status.SUCCEEDED

    fun allowScreenshot() =
        ( (name != WaitTask::class.simpleName)
            && (name != NoopTask::class.simpleName)
            && (name != BehaviorCompleteControlTask::class.simpleName)
        )
}
