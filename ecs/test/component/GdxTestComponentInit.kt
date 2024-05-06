package component

import TestEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import river.exertion.thenuim.asset.immersionTimer.ImmersionTimerPair
import river.exertion.thenuim.base.GdxTestBase
import river.exertion.thenuim.ecs.EngineHandler
import river.exertion.thenuim.ecs.component.IComponent
import river.exertion.thenuim.ecs.component.ImmersionTimerComponent

class GdxTestComponentInit : GdxTestBase()  {

    @BeforeAll
    fun loadSystem() {
        EngineHandler.addSystem(TestComponentSystem())
    }

    @Test
    fun testInitComponent() {
        EngineHandler.instantiateEntity<TestEntity>()

        IComponent.ecsInit<ImmersionTimerComponent>(TestEntity.EntityName, ImmersionTimerPair().apply {this.cumlImmersionTimer.startOrResumeTimer()})

        val componentIsPresent = EngineHandler.hasComponent<ImmersionTimerComponent>(EngineHandler.entityByName(TestEntity.EntityName))
        val presentComponent = EngineHandler.getComponentFor<ImmersionTimerComponent>(EngineHandler.entityByName(TestEntity.EntityName))

        Assertions.assertTrue(componentIsPresent)
        Assertions.assertNotNull(presentComponent)

        runBlocking { delay(8000L) }
    }
}