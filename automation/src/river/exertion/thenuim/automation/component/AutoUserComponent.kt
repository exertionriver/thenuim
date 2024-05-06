package river.exertion.thenuim.automation.component

import river.exertion.thenuim.base.Id
import river.exertion.thenuim.ecs.component.IComponent

@Suppress("NewApi")
class AutoUserComponent : IComponent {

    override var componentId = Id.randomId()
    override var isInitialized = false
}
