package river.exertion.kcop.system.ecs.entity

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity

interface IEntity {

    var entityName : String

    var isInitialized : Boolean

    fun initialize(entity: Entity, initData: Any?) {

        components.forEach {
            if (!entity.components.contains(it) ) entity.add(it)
        }

        isInitialized = true
    }

    //overall narrative timeline
    var components : MutableList<Component>

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
}