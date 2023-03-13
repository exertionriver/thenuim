package river.exertion.kcop.system.ecs.component

interface IComponent {

    var isInitialized : Boolean

    fun initialize(initData: Any?) {

        isInitialized = true
    }
}