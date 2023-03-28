package river.exertion.kcop.system.ecs.entity

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import river.exertion.kcop.assets.ProfileAsset
import river.exertion.kcop.system.ecs.component.IRLTimeComponent
import river.exertion.kcop.system.ecs.component.ProfileComponent

class ProfileEntity : IEntity {

    override var entityName = "profile"
    override var isInitialized = false

//    var assetPath : String? = null

    override fun initialize(entity: Entity, initData: Any?) {
        super.initialize(entity, initData)

        try {
            val profileAsset = IEntity.checkInitType<ProfileAsset>(initData)
            this.entityName = profileAsset?.profile?.id ?: throw Exception("profileEntity:$this entityName not set")
            ProfileComponent.getFor(entity)!!.initialize(entityName, profileAsset.profile)
        } catch (ex : Exception) {
            val entityName = IEntity.checkInitType<String>(initData)
            this.entityName = entityName ?: throw Exception("profileEntity:$this entityName not set")
            ProfileComponent.getFor(entity)!!.initialize(entityName, null)
        }

  //      this.assetPath = profileAsset.assetPath ?: throw Exception("profileEntity:$this assetPath not set")

    }

    override var components : MutableList<Component> = mutableListOf(
        IRLTimeComponent(),
        ProfileComponent()
    )
}