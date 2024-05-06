package river.exertion.thenuim.ecs.component

import river.exertion.thenuim.asset.irlTime.IrlTime
import river.exertion.thenuim.base.Id

@Suppress("NewApi")
class IrlTimeComponent : IComponent {

    override var componentId = Id.randomId()
    override var isInitialized = false

    fun localTime() = IrlTime.localTime()
}