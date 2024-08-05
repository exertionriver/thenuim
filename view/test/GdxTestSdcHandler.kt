import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import river.exertion.thenuim.asset.view.ColorPalette
import river.exertion.thenuim.base.GdxTestBase
import river.exertion.thenuim.base.Log
import river.exertion.thenuim.view.TnmSkin
import river.exertion.thenuim.view.SdcHandler

class GdxTestSdcHandler : GdxTestBase() {

    @AfterEach
    fun unload() {
        SdcHandler.dispose()
    }

    private val initAlpha = .45f
    private val initCP = ColorPalette.of("mediumBlue")

    @Test
    fun testUpdoradBackgroundAlpha() {

        val updoradBgaUpdate = .65f

        Log.test("updoradBackgroundAlpha", "default")
        SdcHandler.updoradBackgroundAlpha("bg_alpha1", initAlpha)
        assertTrue(SdcHandler.has("bg_alpha1"))

        //no params resets to default
        val resetBga = SdcHandler.updoradBackgroundAlpha("bg_alpha1")
        assertEquals(TnmSkin.BackgroundColor.color(), resetBga.baseColor)
        assertEquals(1f, resetBga.alpha)

        Log.test("updoradBackgroundAlpha", "update")
        SdcHandler.updoradBackgroundAlpha("bg_alpha2", initAlpha)
        assertTrue(SdcHandler.has("bg_alpha2"))

        //params update alpha value only
        val updBga = SdcHandler.updoradBackgroundAlpha("bg_alpha2", updoradBgaUpdate)
        assertEquals(TnmSkin.BackgroundColor.color(), updBga.baseColor)
        assertEquals(updoradBgaUpdate, updBga.alpha)
    }

    @Test
    fun testUpdorad() {

        val updoradAlphaUpdate = .65f
        val updoradCPUpdate = ColorPalette.Color246

        Log.test("updorad", "default")
        SdcHandler.updorad("updorad_1", initCP, initAlpha)
        assertTrue(SdcHandler.has("updorad_1"))

        //no params resets to default
        val resetUpd = SdcHandler.updorad("updorad_1")
        assertEquals(TnmSkin.BackgroundColor.color(), resetUpd.baseColor)
        assertEquals(1f, resetUpd.alpha)

        Log.test("updorad", "update")
        SdcHandler.updorad("updorad_2", initCP, initAlpha)
        assertTrue(SdcHandler.has("updorad_2"))

        //params update color and alpha values
        val upd = SdcHandler.updorad("updorad_2", updoradCPUpdate, updoradAlphaUpdate)
        assertEquals(updoradCPUpdate.color(), upd.baseColor)
        assertEquals(updoradAlphaUpdate, upd.alpha)
    }

    @Test
    fun testGetorad() {

        Log.test("getorad", "default")
        SdcHandler.getorad("getorad_1")
        assertTrue(SdcHandler.has("getorad_1"))

        //no params sets to default
        val gDefault = SdcHandler.updoradBackgroundAlpha("getorad_1")
        assertEquals(TnmSkin.BackgroundColor.color(), gDefault.baseColor)
        assertEquals(1f, gDefault.alpha)

        Log.test("getorad", "set")
        SdcHandler.getorad("getorad_2", initCP, initAlpha)
        assertTrue(SdcHandler.has("getorad_2"))

        //getorad does not alter values
        val getSet = SdcHandler.getorad("getorad_2")
        assertEquals(initCP.color(), getSet.baseColor)
        assertEquals(initAlpha, getSet.alpha)
    }
}