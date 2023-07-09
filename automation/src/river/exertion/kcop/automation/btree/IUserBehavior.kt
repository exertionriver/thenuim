package river.exertion.kcop.automation.btree

import com.badlogic.gdx.ai.btree.BehaviorTree

interface IUserBehavior {

    val name : String

    fun btree(autoUser : AutoUser) : BehaviorTree<AutoUser>
}