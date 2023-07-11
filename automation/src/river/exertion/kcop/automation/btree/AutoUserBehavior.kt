package river.exertion.kcop.automation.btree

import com.badlogic.gdx.ai.btree.BehaviorTree

abstract class AutoUserBehavior {

    abstract val name : String

    var behaviorComplete : Boolean = false

    abstract fun btree(autoUser : AutoUser) : BehaviorTree<AutoUser>
}