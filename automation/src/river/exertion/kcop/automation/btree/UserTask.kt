package river.exertion.kcop.automation.btree

import com.badlogic.gdx.ai.btree.LeafTask
import com.badlogic.gdx.ai.btree.Task
import river.exertion.kcop.base.Log

abstract class UserTask : LeafTask<AutoUser>() {

    abstract val name : String

    override fun copyTo(task: Task<AutoUser>?): Task<AutoUser> {
        TODO("Not yet implemented")
    }

    override fun execute(): Status {
        val autoUser  = this.`object` as AutoUser
        Log.debug(autoUser.name, "running ${autoUser.currentBehavior.name}, ${this.name}")
        return Status.SUCCEEDED
    }
}