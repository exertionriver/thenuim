import river.exertion.kcop.asset.immersionTimer.ImmersionTimerPair
import river.exertion.kcop.base.Id
import river.exertion.kcop.ecs.component.IComponent

class TestComponent : IComponent {

    override var componentId = Id.randomId()
    override var isInitialized = false

    var componentName = ComponentName

    override fun initialize(initData: Any?) {
        val initName = IComponent.checkInitType<String>(initData)

        if (initName != null) {
            componentName = initName

            super.initialize(initData)
        }
    }

    companion object {
        const val ComponentName = "TestComponent"
    }
}