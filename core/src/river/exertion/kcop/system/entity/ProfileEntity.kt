package river.exertion.kcop.system.entity

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import kotlinx.serialization.json.encodeToJsonElement
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.mapperFor
import ktx.ashley.with
import river.exertion.kcop.Util
import river.exertion.kcop.assets.ProfileAsset
import river.exertion.kcop.system.SystemManager
import river.exertion.kcop.system.component.IRLTimeComponent
import river.exertion.kcop.system.component.ImmersionTimerComponent
import river.exertion.kcop.system.component.ProfileComponent
import river.exertion.kcop.system.profile.Profile

class ProfileEntity : IEntity {

    override var entityName = "profile"
    override var entity : Entity? = null

    override var isInitialized = false

    var assetPath : String? = null

    override fun initialize(entity: Entity, initData: Any?) {
        super.initialize(entity, initData)

        val profileAsset = if (initData != null) initData as ProfileAsset else null

        this.entityName = this.entityName + (profileAsset?.profile?.id ?: "")
        this.assetPath = profileAsset?.assetPath ?: ""

        ProfileComponent.getFor(entity)!!.profile = profileAsset?.profile ?: Profile()
        ImmersionTimerComponent.getFor(entity)!!.cumlImmersionTimer.resumeTimer()
    }

    override var components = mutableListOf(
        IRLTimeComponent(),
        ImmersionTimerComponent(),
        ProfileComponent()
    )

    fun reloadProfile() {
        if (isInitialized) {
            val jsonProfile = Util.json.encodeToJsonElement(ProfileComponent.getFor(entity)!!.profile)
            Gdx.files.local(assetPath).writeString(jsonProfile.toString(), false)
        }
    }

    fun saveProfile() {
        if (isInitialized) {
            val jsonProfile = Util.json.encodeToJsonElement(ProfileComponent.getFor(entity)!!.profile)
            Gdx.files.local(assetPath).writeString(jsonProfile.toString(), false)
        }
    }

    companion object {
        fun has(entity : Entity?) : Boolean { return entity?.components?.firstOrNull{ it is ProfileEntity } != null }
        fun getFor(entity : Entity?) : ProfileEntity? = if (has(entity)) entity?.components?.firstOrNull { it is ProfileEntity } as ProfileEntity else null
    }
}