package river.exertion.kcop.system.ecs.entity

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import river.exertion.kcop.assets.ProfileAsset
import river.exertion.kcop.system.ecs.component.IRLTimeComponent
import river.exertion.kcop.system.ecs.component.ProfileComponent
import river.exertion.kcop.system.profile.Profile

class ProfileEntity : IEntity {

    override var entityName = "profile"

    override var isInitialized = false

    var assetPath : String? = null

    override fun initialize(entity: Entity, initData: Any?) {
        super.initialize(entity, initData)

        val profileAsset = if ((initData != null) && (initData is ProfileAsset) ) initData else null

        this.entityName = profileAsset?.profile?.id ?: ""
        this.assetPath = profileAsset?.assetPath ?: ""

        ProfileComponent.getFor(entity)!!.profile = profileAsset?.profile ?: Profile()
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