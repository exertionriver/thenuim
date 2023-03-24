package river.exertion.kcop.system.ecs.entity

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import river.exertion.kcop.assets.ProfileAsset
import river.exertion.kcop.system.ecs.component.IRLTimeComponent
import river.exertion.kcop.system.ecs.component.NarrativeComponent
import river.exertion.kcop.system.ecs.component.ProfileComponent
import river.exertion.kcop.system.profile.Profile
import kotlin.reflect.KClass

class ProfileEntity : IEntity {

    override var entityName = "profile"
    override var isInitialized = false

    var assetPath : String? = null

    override fun initialize(entity: Entity, initData: Any?) {
        super.initialize(entity, initData)

        val profileAsset = IEntity.checkInitType<ProfileAsset>(initData)

        this.entityName = profileAsset?.profile?.id ?: throw Exception("profileEntity:$this entityName not set")
        this.assetPath = profileAsset.assetPath ?: throw Exception("profileEntity:$this assetPath not set")

        ProfileComponent.getFor(entity)!!.initialize(entityName, profileAsset.profile)
    }

    override var components : MutableList<Component> = mutableListOf(
        IRLTimeComponent(),
        ProfileComponent()
    )

    fun reloadProfile() {
        if (isInitialized) {
//            val jsonProfile = Util.json.encodeToJsonElement(ProfileComponent.getFor(entity)!!.profile)
//            Gdx.files.local(assetPath).writeString(jsonProfile.toString(), false)
        }
    }

    fun saveProfile() {
        if (isInitialized) {
//            val jsonProfile = Util.json.encodeToJsonElement(ProfileComponent.getFor(entity)!!.profile)
//            Gdx.files.local(assetPath).writeString(jsonProfile.toString(), false)
        }
    }
}