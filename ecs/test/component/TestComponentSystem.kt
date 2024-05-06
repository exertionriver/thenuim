package component

import TestEntity
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IntervalIteratingSystem
import ktx.ashley.allOf
import org.junit.jupiter.api.Assertions.assertEquals
import river.exertion.thenuim.base.Log
import river.exertion.thenuim.ecs.EngineHandler
import river.exertion.thenuim.ecs.component.ImmersionTimerComponent

class TestComponentSystem : IntervalIteratingSystem(allOf(ImmersionTimerComponent::class).get(), 9/10f) {

    override fun processEntity(entity: Entity?) {

        if (entity != null) {
            val iEntity = EngineHandler.iEntityByEntity(entity)
            Log.test("entity with iEntity found with ImmersionTimerComponent running TestComponentSystem",
                "entityName:${iEntity.entityName}, componentId:${EngineHandler.getComponentFor<ImmersionTimerComponent>(entity)?.cumlImmersionTime()}")

            assertEquals(iEntity.entityName, TestEntity.EntityName)
        }
    }
}