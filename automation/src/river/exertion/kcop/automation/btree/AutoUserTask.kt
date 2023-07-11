package river.exertion.kcop.automation.btree

import com.badlogic.gdx.ai.btree.LeafTask
import com.badlogic.gdx.ai.btree.Task
import river.exertion.kcop.base.Log

abstract class AutoUserTask : LeafTask<AutoUser>() {

    val name : String = this::class.simpleName!!

    override fun copyTo(task: Task<AutoUser>?): Task<AutoUser> {
        TODO("Not yet implemented")
    }

    //executes in context of task queue
    abstract fun executeTask()

    //executes in context of btree
    override fun execute(): Status {
        AutoUserBehaviorHandler.taskSequenceList.add(this)
        Log.debug("loading ${AutoUserBehaviorHandler.currentBehavior.name}", this.name)
        return returnStatus()
    }

    open fun returnStatus() = Status.SUCCEEDED
}
