package river.exertion.kcop.automation.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import river.exertion.kcop.automation.btree.AutoUserBehaviorHandler
import river.exertion.kcop.automation.component.AutoUserComponent

class AutoUserSystem : IteratingSystem(allOf(AutoUserComponent::class).get()) {

    override fun processEntity(entity: Entity, deltaTime : Float) {

        if (AutoUserBehaviorHandler.taskSequenceList.isNotEmpty()) {

            AutoUserBehaviorHandler.taskSequenceList.first {
                it.executeTask()
                AutoUserBehaviorHandler.taskLogList.add(it)
            }

            AutoUserBehaviorHandler.taskSequenceList.removeFirst()

        }
    }
}