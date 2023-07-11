package river.exertion.kcop.ecs.component

import river.exertion.kcop.asset.irlTime.IrlTime
import river.exertion.kcop.base.Id
import river.exertion.kcop.ecs.EngineHandler

@Suppress("NewApi")
class IrlTimeComponent : IComponent {

    override var componentId = Id.randomId()
    override var isInitialized = false

    fun localTime() = IrlTime.localTime()
}