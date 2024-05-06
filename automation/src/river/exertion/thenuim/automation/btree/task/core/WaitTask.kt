package river.exertion.thenuim.automation.btree.task.core

import com.badlogic.gdx.Gdx
import river.exertion.thenuim.automation.btree.AutoUserTask
import java.util.*

class WaitTask(private val waitSeconds : Float) : AutoUserTask() {

    override val id = UUID.fromString("fbc8c300-f888-4b80-90b6-257606f2187e")

    override fun executeTask() { }

    override fun execute(): Status {
        val noopsToAdd = (waitSeconds / Gdx.graphics.deltaTime).toInt()

        repeat(noopsToAdd) {
            river.exertion.thenuim.automation.AutoUserTestHandler.currentBehavior.readyTasks.add(NoopTask())
        }

        return super.execute()
    }
}
