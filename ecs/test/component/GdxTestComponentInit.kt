package component

import TestEntity
import com.badlogic.gdx.Gdx
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import river.exertion.kcop.asset.immersionTimer.ImmersionTimer
import river.exertion.kcop.asset.immersionTimer.ImmersionTimerPair
import river.exertion.kcop.base.GdxTestBase
import river.exertion.kcop.base.KcopBase
import river.exertion.kcop.base.Log
import river.exertion.kcop.ecs.EngineHandler
import river.exertion.kcop.ecs.component.ImmersionTimerComponent

class GdxTestComponentInit : GdxTestBase()  {

    @BeforeAll
    fun loadSystem() {
        EngineHandler.addSystem(TestComponentSystem())
    }

    @Test
    fun testInitComponent() {
        EngineHandler.instantiateEntity<TestEntity>()

        ImmersionTimerComponent.ecsInit(TestEntity.EntityName, ImmersionTimerPair().apply {this.cumlImmersionTimer.startOrResumeTimer()})

        val componentIsPresent = EngineHandler.hasComponent<ImmersionTimerComponent>(EngineHandler.entityByName(TestEntity.EntityName))
        val presentComponent = EngineHandler.getComponentFor<ImmersionTimerComponent>(EngineHandler.entityByName(TestEntity.EntityName))

        Assertions.assertTrue(componentIsPresent)
        Assertions.assertNotNull(presentComponent)

        runBlocking { delay(8000L) }
    }
}