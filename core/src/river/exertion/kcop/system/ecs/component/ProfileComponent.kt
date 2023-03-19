package river.exertion.kcop.system.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import river.exertion.kcop.system.profile.Profile

class ProfileComponent : IComponent {

    var profile : Profile? = null

    override var entityName = ""
    override var isInitialized = false

    override fun initialize(entityName: String, initData: Any?) {
        super.initialize(entityName, initData)

        if (initData != null) {
            profile = IComponent.checkInitType(initData)
        }
    }

    companion object {
        fun has(entity : Entity?) : Boolean = entity?.components?.firstOrNull{ it is ProfileComponent } != null
        fun getFor(entity : Entity?) : ProfileComponent? = if (has(entity)) entity?.components?.firstOrNull { it is ProfileComponent } as ProfileComponent else null
    }

}