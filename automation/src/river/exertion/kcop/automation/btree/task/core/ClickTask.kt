package river.exertion.kcop.automation.btree.task.core

import com.badlogic.gdx.math.Vector2
import river.exertion.kcop.automation.btree.AutoUserTask
import river.exertion.kcop.base.KcopBase
import river.exertion.kcop.base.Log
import java.util.*

open class ClickTask(open val screenX : Int, open val screenY : Int, open val pointer : Int? = 0, open val button : Int? = 0) : AutoUserTask() {

    override val id = UUID.fromString("e5b40825-ae6d-4651-a1dc-0b69452b9270")

    constructor(screenXY: Vector2) : this(screenXY.x.toInt(), screenXY.y.toInt())

    override fun executeTask() {
        Log.debug("click at ($screenX, $screenY) with input-button $button")

        KcopBase.inputMultiplexer.touchDown(screenX, screenY, pointer!!, button!!)
        KcopBase.inputMultiplexer.touchUp(screenX, screenY, pointer!!, button!!)
    }
}
