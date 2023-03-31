package river.exertion.kcop.system.ecs.entity

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver
import river.exertion.kcop.assets.NarrativeAssets
import river.exertion.kcop.assets.ProfileAsset
import river.exertion.kcop.assets.ProfileAssetLoader
import river.exertion.kcop.system.ecs.component.IComponent
import river.exertion.kcop.system.ecs.component.NarrativeComponent
import kotlin.io.path.Path

class NarrativeEntity : IEntity {

    override var entityName = "<default>"
    override var isInitialized = false

    override fun initialize(entity: Entity, initData: Any?) {
        super.initialize(entity, initData)

        val narrativeComponentInit = IComponent.checkInitType<NarrativeComponent.NarrativeComponentInit>(initData)

        this.entityName = narrativeComponentInit?.narrativeAsset?.narrative?.name ?: throw Exception("narrativeEntity:$this entityName not set")

        NarrativeComponent.getFor(entity)!!.initialize(entityName, initData)
    }

    //overall narrative timeline
    override var components: MutableList<Component> = mutableListOf(
        NarrativeComponent()
    )
}