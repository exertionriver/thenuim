import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import river.exertion.kcop.base.Log
import river.exertion.kcop.base.str

class TestLog {

    @Test
    fun testLogDebug() {
        assert(Log.debug("tag", "message") == "${Log.debugPrefix} tag: message")
    }

    @Test
    fun testLogTest() {
        assert(Log.test("tag", "message") == "${Log.testPrefix} tag: message")
    }

    @Test
    fun testStr() {

        val testInfo = listOf("test", "info", "strings")

        val testInfoStr = testInfo.str("\t")

        assertEquals("test\tinfo\tstrings", testInfoStr)

        Log.test("testInfoStr", testInfoStr)
    }
}