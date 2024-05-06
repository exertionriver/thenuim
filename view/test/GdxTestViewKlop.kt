import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import river.exertion.thenuim.asset.AssetManagerHandler
import river.exertion.thenuim.base.GdxTestBase
import river.exertion.thenuim.base.KcopBase
import river.exertion.thenuim.base.Log
import river.exertion.thenuim.ecs.EngineHandler
import river.exertion.thenuim.messaging.MessageChannelHandler
import river.exertion.thenuim.view.ViewKlop
import river.exertion.thenuim.view.asset.*
import river.exertion.thenuim.view.layout.ViewLayout
import river.exertion.thenuim.view.menu.DisplayViewMenuHandler

class GdxTestViewKlop : GdxTestBase() {

    @BeforeEach
    fun loadKlop() {
        ViewKlop.load()
    }

    @AfterEach
    fun unloadKlop() {
        ViewKlop.dispose()
    }

    @Test
    fun testLoadedSizes() {
        Log.test("loaded counts")

        val channelsSize = 1

        Log.test("MessageChannelHandler", MessageChannelHandler.size().toString())
        assertEquals(channelsSize, MessageChannelHandler.size())

        //ftf is doubled since it generates bitmapFont assets
        val assetsSize = 2 * FreeTypeFontAssetStore.entries.size +
                MusicAssetStore.entries.size +
                // skin loads .json, .png, .atlas
                3 * SkinAssetStore.entries.size +
                SoundAssetStore.entries.size +
                TextureAssetStore.entries.size

        Log.test("AssetManagerHandler", AssetManagerHandler.loadedSize().toString())
        assertEquals(assetsSize, AssetManagerHandler.loadedSize())

        val systemsSize = 1

        Log.test("EngineHandler (systems)", EngineHandler.systemsSize().toString())
        assertEquals(systemsSize, EngineHandler.systemsSize())

        val displayViewMenusSize = 1

        Log.test("DisplayViewMenus", DisplayViewMenuHandler.size().toString())
        assertEquals(displayViewMenusSize, DisplayViewMenuHandler.size())
    }

    override fun render() {
        super.render()

        val hitActor = KcopBase.stage.hit(100f, 100f, true)

        Log.test(hitActor.toString())

    }

    @Test
    fun testHits() {
        gdxTestRunning = true

        ViewLayout.build(KcopBase.stage)
    }
}