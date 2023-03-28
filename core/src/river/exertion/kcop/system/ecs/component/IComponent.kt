package river.exertion.kcop.system.ecs.component

import com.badlogic.ashley.core.Component

interface IComponent : Component {

    var isInitialized : Boolean
    var entityName : String

    //done automatically by EngineHandler::addComponent()
    fun initialize(entityName : String, initData: Any?) {
        this.entityName = entityName
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