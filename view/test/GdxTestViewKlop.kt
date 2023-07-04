import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import river.exertion.kcop.asset.AssetManagerHandler
import river.exertion.kcop.base.GdxTestBase
import river.exertion.kcop.base.Log
import river.exertion.kcop.ecs.EngineHandler
import river.exertion.kcop.messaging.MessageChannelHandler
import river.exertion.kcop.view.SdcHandler
import river.exertion.kcop.view.ViewKlop
import river.exertion.kcop.view.asset.*
import river.exertion.kcop.view.menu.DisplayViewMenuHandler

class GdxTestViewKlop : GdxTestBase() {

    var testRunning = false

    @BeforeAll
    fun init() {
        init(this)
    }

    @BeforeEach
    fun loadKlop() {
        ViewKlop.load()
    }

    @AfterEach
    fun unloadKlop() {
        testRunning = false

        ViewKlop.dispose()
    }

    override fun create() {
        super.create()
        SdcHandler.batch = twoBatch
    }

    @Test
    fun testLoadedSizes() {
        Log.test("loaded counts")

        val channelsSize = 1

        Log.test("MessageChannelHandler", MessageChannelHandler.size().toString())
        assertEquals(channelsSize, MessageChannelHandler.size())

        //ftf is doubled since it generates bitmapFont assets
        val assetsSize = 2 * FreeTypeFontAssetStore.values().size +
                MusicAssetStore.values().size +
                // skin loads .json, .png, .atlas
                3 * SkinAssetStore.values().size +
                SoundAssetStore.values().size +
                TextureAssetStore.values().size

        Log.test("AssetManagerHandler", AssetManagerHandler.loadedSize().toString())
        assertEquals(assetsSize, AssetManagerHandler.loadedSize())

        val systemsSize = 1

        Log.test("EngineHandler (systems)", EngineHandler.systemsSize().toString())
        assertEquals(systemsSize, EngineHandler.systemsSize())

        val displayViewMenusSize = 1

        Log.test("DisplayViewMenus", DisplayViewMenuHandler.size().toString())
        assertEquals(displayViewMenusSize, DisplayViewMenuHandler.size())
    }
}