import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import river.exertion.thenuim.ecs.entity.IEntity

class TestEntity : IEntity {

    override var entityName = EntityName
    override var isInitialized = false

    override var components : MutableList<Component> = mutableListOf()

    override fun initialize(entity: Entity, initData: Any?) {
        val initName = IEntity.checkInitType<String>(initData)

        if (initName != null) {
            entityName = initName

            super.initialize(entity, initData)
        }
    }

    companion object {
        const val EntityName = "testEntity"
    }
}