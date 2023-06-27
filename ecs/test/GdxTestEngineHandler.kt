import com.badlogic.gdx.Gdx
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import river.exertion.kcop.base.GdxTestBase
import river.exertion.kcop.base.Id
import river.exertion.kcop.base.Log
import river.exertion.kcop.ecs.EngineHandler
import river.exertion.kcop.ecs.klop.IECSKlop

class GdxTestEngineHandler : IECSKlop, GdxTestBase() {

    override var id = Id.randomId()
    override var name = this::class.simpleName.toString()

    var testSystem = TestSystem()
    var testRunning = false

    @BeforeAll
    override fun load() {
        init(this)
    }

    @BeforeEach
    override fun loadSystems() {
        EngineHandler.addSystem(testSystem)
    }

    @AfterEach
    fun removeChannel() {
        testRunning = false
        EngineHandler.dispose()
    }

    @AfterAll
    override fun unload() {
        super.dispose()
    }

    override fun render() {
        if (testRunning) {
            Log.test("render() delta time", Gdx.app.graphics.deltaTime.toString())
            EngineHandler.update(Gdx.app.graphics.deltaTime)
        }
    }

    @Test
    fun testInitRemoveEntity() {
        Log.test("instantiating ${TestEntity.EntityName}")
        EngineHandler.instantiateEntity<TestEntity>()

        EngineHandler.entityByName(TestEntity.EntityName)
        Log.test(TestEntity.EntityName, "found!")

        val byWrongNameException = assertThrows(Exception::class.java) { EngineHandler.entityByName(TestEntity.EntityName + "notFound") }
        Log.test("byName() exception correctly thrown for bad name", byWrongNameException.message)

        Log.test("removing ${TestEntity.EntityName} by name")
        EngineHandler.removeEntity(TestEntity.EntityName)

        var byRightNameException = assertThrows(Exception::class.java) { EngineHandler.entityByName(TestEntity.EntityName) }
        Log.test("byName() exception correctly thrown for removed, good name", byRightNameException.message)

        Log.test("instantiating ${TestEntity.EntityName} again")
        EngineHandler.instantiateEntity<TestEntity>()

        EngineHandler.entityByName(TestEntity.EntityName)
        Log.test(TestEntity.EntityName, "found!")

        Log.test("removing ${TestEntity.EntityName} by class")
        EngineHandler.removeEntity(TestEntity::class)

        byRightNameException = assertThrows(Exception::class.java) { EngineHandler.entityByName(TestEntity.EntityName) }
        Log.test("byName() exception correctly thrown for removed, good name", byRightNameException.message)

        Log.test("instantiating ${TestEntity.EntityName} again")
        EngineHandler.instantiateEntity<TestEntity>()

        EngineHandler.entityByName(TestEntity.EntityName)
        Log.test(TestEntity.EntityName, "found!")

        Log.test("removing ${TestEntity.EntityName} by IEntity")
        EngineHandler.removeEntity(EngineHandler.iEntityByName(TestEntity.EntityName))

        byRightNameException = assertThrows(Exception::class.java) { EngineHandler.entityByName(TestEntity.EntityName) }
        Log.test("byName() exception correctly thrown for removed, good name", byRightNameException.message)

        Log.test("initializing ${TestEntity.EntityName}")
        val entityInitName = "entityInitName"
        EngineHandler.instantiateEntity<TestEntity>(entityInitName)

        EngineHandler.entityByName(entityInitName)
        Log.test(entityInitName, "found!")
    }

    @Test
    fun badInitEntity() {
        val integer : Int = 123

        val initException = assertThrows(Exception::class.java) { EngineHandler.instantiateEntity<TestEntity>(integer) }
        Log.test("init exception", "correctly thrown for wrong init type: ${initException.cause}")
    }

    @Test
    fun testAddInitRemoveComponent() {

        Log.test("instantiating ${TestEntity.EntityName}")
        EngineHandler.instantiateEntity<TestEntity>()

        var componentIsPresent = EngineHandler.hasComponent<TestComponent>(EngineHandler.entityByName(TestEntity.EntityName))
        var presentComponent = EngineHandler.getComponentFor<TestComponent>(EngineHandler.entityByName(TestEntity.EntityName))

        assertFalse(componentIsPresent)
        assertNull(presentComponent)

        Log.test("adding component", TestComponent.ComponentName)
        EngineHandler.addComponent<TestComponent>(TestEntity.EntityName)

        componentIsPresent = EngineHandler.hasComponent<TestComponent>(EngineHandler.entityByName(TestEntity.EntityName))
        presentComponent = EngineHandler.getComponentFor<TestComponent>(EngineHandler.entityByName(TestEntity.EntityName))

        assertTrue(componentIsPresent)
        assertEquals(TestComponent.ComponentName, presentComponent?.componentName)

        Log.test("removing component", TestComponent.ComponentName)
        EngineHandler.removeComponent<TestComponent>(TestEntity.EntityName)

        componentIsPresent = EngineHandler.hasComponent<TestComponent>(EngineHandler.entityByName(TestEntity.EntityName))
        presentComponent = EngineHandler.getComponentFor<TestComponent>(EngineHandler.entityByName(TestEntity.EntityName))

        assertFalse(componentIsPresent)
        assertNull(presentComponent)

        val manualInitComponentName = "manualInitComponentName"
        val manualInitComponent = TestComponent().apply { this.componentName = "manualInitComponentName" }

        Log.test("adding manual init component", manualInitComponent.componentName)
        EngineHandler.addComponentInstance<TestComponent>(TestEntity.EntityName, manualInitComponent)

        presentComponent = EngineHandler.getComponentFor<TestComponent>(EngineHandler.entityByName(TestEntity.EntityName))
        assertNotNull(presentComponent)
        assertEquals(manualInitComponentName, presentComponent?.componentName)

        val initComponentName = "initComponentName"

        Log.test("replacing with init component", manualInitComponent.componentName)
        EngineHandler.replaceComponent<TestComponent>(TestEntity.EntityName, initComponentName)

        presentComponent = EngineHandler.getComponentFor<TestComponent>(EngineHandler.entityByName(TestEntity.EntityName))
        assertNotNull(presentComponent)
        assertEquals(initComponentName, presentComponent?.componentName)

        Log.test("testing add component when already added", initComponentName)
        EngineHandler.addComponent<TestComponent>(TestEntity.EntityName, initComponentName)
        presentComponent = EngineHandler.getComponentFor<TestComponent>(EngineHandler.entityByName(TestEntity.EntityName))
        assertEquals(initComponentName, presentComponent?.componentName)

        EngineHandler.addComponentInstance<TestComponent>(TestEntity.EntityName, manualInitComponent)
        presentComponent = EngineHandler.getComponentFor<TestComponent>(EngineHandler.entityByName(TestEntity.EntityName))
        assertEquals(initComponentName, presentComponent?.componentName)

    }

    @Test
    fun badInitComponent() {
        val integer : Int = 123

        EngineHandler.instantiateEntity<TestEntity>()

        val initException = assertThrows(Exception::class.java) { EngineHandler.addComponent<TestComponent>(initInfo = integer) }
        Log.test("init exception", "correctly thrown for wrong init type: ${initException.cause}")
    }

    @Test
    fun testSystem() {
        testRunning = true

        EngineHandler.instantiateEntity<TestEntity>(TestSystemEntityInitName)

        val componentInitName = "testSystemComponentInitName"
        EngineHandler.addComponent<TestComponent>(TestSystemEntityInitName, componentInitName)

        runBlocking { delay(2000L) }
    }

    companion object {
        const val TestSystemEntityInitName = "testSystemEntityInitName"
    }
}