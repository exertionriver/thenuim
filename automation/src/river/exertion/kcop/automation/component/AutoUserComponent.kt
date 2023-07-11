package river.exertion.kcop.automation.component

import river.exertion.kcop.automation.btree.AutoUser
import river.exertion.kcop.base.Id
import river.exertion.kcop.ecs.EngineHandler
import river.exertion.kcop.ecs.component.IComponent

@Suppress("NewApi")
class AutoUserComponent : IComponent {

    override var componentId = Id.randomId()
    override var isInitialized = false

    val autoUser = AutoUser()

}