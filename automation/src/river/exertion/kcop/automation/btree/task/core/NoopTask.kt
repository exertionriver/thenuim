package river.exertion.kcop.automation.btree.task.core

import com.badlogic.gdx.Gdx
import river.exertion.kcop.automation.btree.AutoUserTask
import river.exertion.kcop.base.Log
import java.util.*

class NoopTask : AutoUserTask() {

    override val id = UUID.fromString("5e86b0d6-d101-462a-b952-59060352ffa9")

    override fun executeTask() {
//        Log.debug("no-op for ${Gdx.graphics.deltaTime}s")
    }
}
