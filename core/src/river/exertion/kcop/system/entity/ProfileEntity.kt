package river.exertion.kcop.system.entity

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.mapperFor
import ktx.ashley.with
import river.exertion.kcop.Id
import river.exertion.kcop.Util
import river.exertion.kcop.assets.ProfileAsset
import river.exertion.kcop.system.SystemManager
import river.exertion.kcop.system.component.IRLTimeComponent
import river.exertion.kcop.system.component.ImmersionTimerComponent
import river.exertion.kcop.system.component.NarrativeComponent
import river.exertion.kcop.system.component.ProfileComponent
import river.exertion.kcop.system.immersionTimer.ImmersionTimerState
import river.exertion.kcop.system.profile.Profile

class ProfileEntity : Component, Id() {

    var entityName = "profile"
    lateinit var entity : Entity

    var isInitialized = false
    var assetPath : String? = null

    fun initialize(entity: Entity, profileAsset : ProfileAsset) {
        this.entity = entity
        this.entityName = this.entityName + (profileAsset.profile?.id ?: "")
        this.assetPath = profileAsset.assetPath

        components.forEach {
            if (!entity.components.contains(it) ) entity.add(it)
        }

        ProfileComponent.getFor(entity)!!.profile = profileAsset.profile
        ImmersionTimerComponent.getFor(entity)!!.cumlImmersionTimer.resumeTimer()

        isInitialized = true
    }

    var components = mutableListOf(
        IRLTimeComponent(),
        ImmersionTimerComponent(),
        ProfileComponent()
    )

    fun saveProfile() {
        if (isInitialized) {
            val jsonProfile = Util.json.encodeToJsonElement(ProfileComponent.getFor(entity)!!.profile)
            Gdx.files.local(assetPath).writeString(jsonProfile.toString(), false)
        }
    }

    companion object {
        val mapper = mapperFor<ProfileEntity>()

        fun has(entity : Entity) : Boolean { return entity.components.firstOrNull{ it is ProfileEntity } != null }
        fun getFor(entity : Entity) : ProfileEntity? = if (has(entity)) entity.components.first { it is ProfileEntity } as ProfileEntity else null

        fun instantiate(engine: PooledEngine, profileAsset : ProfileAsset) : Entity {
            val newProfileEntity = engine.entity {
                with<ProfileEntity>()
            }.apply { this[mapper]?.initialize(this, profileAsset) }

            SystemManager.logDebug (::instantiate.javaClass.name, "${getFor(newProfileEntity)!!.entityName} instantiated! @ ${IRLTimeComponent.getFor(newProfileEntity)!!.localTime()}")
            return newProfileEntity
        }
    }
}