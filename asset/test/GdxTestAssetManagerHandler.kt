import com.badlogic.gdx.Application
import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.headless.HeadlessApplication
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import river.exertion.kcop.asset.AssetManagerHandler
import river.exertion.kcop.asset.AssetManagerHandler.getAssets
import river.exertion.kcop.asset.AssetManagerHandler.lfhr
import river.exertion.kcop.asset.AssetManagerHandler.loadAssets
import river.exertion.kcop.asset.AssetManagerHandler.logAssets
import river.exertion.kcop.asset.AssetStatus
import kotlin.io.path.Path
import kotlin.io.path.deleteIfExists
import kotlin.io.path.extension
import kotlin.io.path.listDirectoryEntries

class GdxTestAssetManagerHandler : ApplicationListener {

    init {
        val conf = HeadlessApplicationConfiguration()
        HeadlessApplication(this, conf)
    }

    var testData : MutableList<TestIAsset> = mutableListOf()

    @BeforeEach
    fun initTestData() {

        testData = mutableListOf(TestIAsset(), TestIAsset(), TestIAsset())

        testData.forEach {
            println("persisting: ${it.assetPath()}")
            AssetManagerHandler.saveAsset<TestIAsset.TestIAssetData>(it)
        }
    }

    @AfterEach
    fun cleanupTestData() {

        testData.forEach {
            Path(it.assetPath()).deleteIfExists()
            println("deleted: ${it.assetPath()}")
        }

        AssetManagerHandler.clearAssets<TestIAsset>()
    }

    @Test
    fun testSaveAsset() {
        val initAssetPaths = testData.map { it.assetPath() }.toSet()

        val assetFiles = Path(TestIAssets.iAssetsLocation).listDirectoryEntries().filter { TestIAssets.iAssetsExtension == it.extension }.map { it.toString() }
        assertEquals(initAssetPaths, assetFiles.toSet())
    }

    @Test
    fun testClearAssets() {

        loadAssets<TestIAsset>(TestIAssets.iAssetsLocation, TestIAssets.iAssetsExtension)

        assertEquals(3, getAssets<TestIAsset>().size)

        AssetManagerHandler.clearAssets<TestIAsset>()

        assertEquals(0, getAssets<TestIAsset>().size)
    }

    @Test
    fun testLoadAssets() {

        val initAssetIds = testData.map { it.assetId() }.toSet()

        assertEquals(0, getAssets<TestIAsset>().size)

        loadAssets<TestIAsset>(TestIAssets.iAssetsLocation, TestIAssets.iAssetsExtension)

        assertEquals(3, getAssets<TestIAsset>().size)

        val loadedAssetIds = getAssets<TestIAsset>().map { it.assetId() }.toSet()

        assertEquals(initAssetIds, loadedAssetIds)
    }

    @Test
    fun testLogAssets() {

        loadAssets<TestIAsset>(TestIAssets.iAssetsLocation, TestIAssets.iAssetsExtension)

        getAssets<TestIAsset>()[0].assetStatus = AssetStatus("assetPath", "error", "failed load test")

        val failedAssets = logAssets<TestIAsset>()

        assertEquals(1, failedAssets.size)
    }

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        AssetManagerHandler.assets.setLoader(TestIAsset::class.java, TestIAssetLoader(lfhr))
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun render() {
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun dispose() {
        AssetManagerHandler.dispose()
    }

}