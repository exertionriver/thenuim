package river.exertion.kcop.automation.btree.task

import com.badlogic.gdx.math.Vector2
import river.exertion.kcop.automation.btree.AutoUserTask
import river.exertion.kcop.base.KcopBase
import river.exertion.kcop.base.Log

open class ClickTask(open val screenX : Int, open val screenY : Int, open val pointer : Int? = 0, open val button : Int? = 0) : AutoUserTask() {

    constructor(screenXY: Vector2) : this(screenXY.x.toInt(), screenXY.y.toInt())

    override fun executeTask() {
        Log.debug("click at ($screenX, $screenY) with input-button $button")

        KcopBase.inputMultiplexer.touchDown(screenX, screenY, pointer!!, button!!)
        KcopBase.inputMultiplexer.touchUp(screenX, screenY, pointer!!, button!!)
    }
}
