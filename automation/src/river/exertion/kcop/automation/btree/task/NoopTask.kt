package river.exertion.kcop.automation.btree.task

import com.badlogic.gdx.Gdx
import river.exertion.kcop.automation.btree.AutoUserTask
import river.exertion.kcop.base.Log

class NoopTask : AutoUserTask() {

    override fun executeTask() {
//        Log.debug("no-op for ${Gdx.graphics.deltaTime}s")
    }
}
