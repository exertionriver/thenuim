package river.exertion.kcop.ecs.component

import com.badlogic.ashley.core.Component

interface IComponent : Component {

    var componentId : String
    var isInitialized : Boolean

    //done automatically by EngineHandler::addComponent()
    fun initialize(initData: Any?) {
        isInitialized = true
    }

    companion object {
        inline fun <reified T:Any>checkInitType(initData : Any?) : T? {
            return if (initData != null) {
                if (initData is T) initData
                else {
                    throw Exception("initData:$this requires ${T::class}, found ${initData::class}")
                }
            }
            else null
        }
    }

    //in case disposables are used
    fun dispose() {}
}