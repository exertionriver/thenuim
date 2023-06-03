import org.junit.jupiter.api.Test
import river.exertion.kcop.asset.AssetManagerHandler

class TestAssetManagerHandler {

    @Test
    fun testLogDebug() {
        assert(AssetManagerHandler.logDebug("tag", "message") == "tag: message")
    }
}