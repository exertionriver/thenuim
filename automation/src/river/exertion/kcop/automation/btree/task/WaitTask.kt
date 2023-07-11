package river.exertion.kcop.automation.btree.task

import com.badlogic.gdx.Gdx
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import river.exertion.kcop.automation.btree.AutoUserBehaviorHandler
import river.exertion.kcop.automation.btree.AutoUserTask
import river.exertion.kcop.base.Log

class WaitTask(private val waitSeconds : Float) : AutoUserTask() {

    override fun executeTask() { }

    override fun execute(): Status {
        val noopsToAdd = (waitSeconds / Gdx.graphics.deltaTime).toInt()

        repeat(noopsToAdd) {
            AutoUserBehaviorHandler.taskSequenceList.add(NoopTask())
        }

        return super.execute()
    }
}
