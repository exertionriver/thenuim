package river.exertion.kcop.base

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import org.junit.jupiter.api.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class TestBase {

    @Suppress("NewApi")
    @BeforeAll
    fun testBaseSuiteBegin(testInfo: TestInfo) {
        if (Gdx.app != null) Gdx.app.logLevel = Application.LOG_DEBUG

        Log.test("begin_suite", testInfo.testClass.get().simpleName)
    }

    @BeforeEach
    fun testBaseBegin(testInfo: TestInfo) {
        Log.test("begin_test", testInfo.displayName)
    }

    @AfterEach
    fun testBaseEnd(testInfo: TestInfo) {
        Log.test("end_test", testInfo.displayName)
    }

    @Suppress("NewApi")
    @AfterAll
    fun testBaseSuiteEnd(testInfo: TestInfo) {
        Log.test("end_suite", testInfo.testClass.get().simpleName)
    }

}