import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import river.exertion.kcop.base.GdxTestBase
import river.exertion.kcop.view.SdcHandler

class GdxTestSdcHandler : GdxTestBase() {

    @AfterEach
    fun unload() {
        SdcHandler.dispose()
    }

    @Test
    fun testGetoradBackgroundAlpha() {

        SdcHandler.getoradBackgroundAlpha("testing bg alpha")

        assertTrue(SdcHandler.has("testing bg alpha"))
    }
}