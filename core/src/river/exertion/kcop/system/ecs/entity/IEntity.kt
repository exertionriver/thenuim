package river.exertion.kcop.system.ecs.entity

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import river.exertion.kcop.assets.ProfileAsset
import river.exertion.kcop.system.messaging.MessageChannel
import river.exertion.kcop.system.messaging.messages.StatusViewMessage
import kotlin.reflect.KClass

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
        inline fun <reified T>checkInitType(initData : Any?) : T? {
            return if ( (initData != null) && (initData is T) ) initData else null
        }
    }
}