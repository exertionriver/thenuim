package river.exertion.kcop.system.ecs.component

interface IComponent {

    var isInitialized : Boolean

    fun initialize(initData: Any?) {

        isInitialized = true
    }

    companion object {
        inline fun <reified T>checkInitType(initData : Any?) : T? {
            return if (initData != null) {
                if (initData is T) initData
                else {
                    throw Exception("receive:$this requires ${T::class}, found ${initData::class}")
                }
            }
            else null
        }
    }

}