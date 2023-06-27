import GdxTestEngineHandler.Companion.TestSystemEntityInitName
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import ktx.ashley.allOf
import org.junit.jupiter.api.Assertions.assertEquals
import river.exertion.kcop.base.Log
import river.exertion.kcop.ecs.EngineHandler

class TestSystem : IntervalIteratingSystem(allOf(TestComponent::class).get(), 1/10f) {
    override fun processEntity(entity: Entity?) {

        if (entity != null) {
            val iEntity = EngineHandler.iEntityByEntity(entity)
            Log.test("entity with iEntity found with TestComponent running TestSystem",
                "entityName:${iEntity.entityName}, componentId:${EngineHandler.getComponentFor<TestComponent>(entity)?.componentId}")

            assertEquals(iEntity.entityName, TestSystemEntityInitName)
        }
    }
}