import com.badlogic.gdx.utils.GdxRuntimeException
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import river.exertion.kcop.asset.AssetManagerHandler
import river.exertion.kcop.asset.AssetManagerHandler.getAsset
import river.exertion.kcop.asset.AssetManagerHandler.getAssets
import river.exertion.kcop.asset.AssetManagerHandler.lfhr
import river.exertion.kcop.asset.AssetManagerHandler.loadAssetByPath
import river.exertion.kcop.asset.AssetManagerHandler.loadAssetsByLocation
import river.exertion.kcop.asset.AssetManagerHandler.loadAssetsByPath
import river.exertion.kcop.asset.AssetManagerHandler.logAssets
import river.exertion.kcop.asset.AssetStatus
import river.exertion.kcop.asset.klop.IAssetKlop
import river.exertion.kcop.base.GdxTestBase
import river.exertion.kcop.base.Id
import river.exertion.kcop.base.Log
import river.exertion.kcop.base.str
import kotlin.io.path.Path
import kotlin.io.path.deleteIfExists
import kotlin.io.path.extension
import kotlin.io.path.listDirectoryEntries

class GdxTestAssetManagerHandler : IAssetKlop, GdxTestBase() {

    private var testData : MutableList<TestIAsset> = mutableListOf()

    override var id = Id.randomId()
    override var tag = this::class.simpleName.toString()

    //used for IAssetKlop overload, does not actually load assets
    @BeforeEach
    override fun loadAssets() {
        AssetManagerHandler.assets.setLoader(TestIAsset::class.java, TestIAssetLoader(lfhr))
    }

    //used for IAssetKlop overload
    @BeforeEach
    override fun load() {
        testData = mutableListOf(TestIAsset(), TestIAsset(), TestIAsset())

        testData.forEach {
            Log.test("persisting", it.assetPath())
            AssetManagerHandler.saveAsset<TestIAsset.TestIAssetData>(it)
        }
    }

    //used for IAssetKlop overload
    @AfterEach
    override fun unload() {
        testData.forEach {
            Path(it.assetPath()).deleteIfExists()
            Log.test("deleted", it.assetPath())
        }

        AssetManagerHandler.clearAssets<TestIAsset>()
    }

    @AfterAll
    override fun dispose() {
        super<IAssetKlop>.dispose()
    }

    @Test
    fun testSaveAsset() {
        val initAssetPaths = testData.map { it.assetPath() }.toSet()

        val assetFiles = Path(TestIAssets.iAssetsLocation).listDirectoryEntries().filter { TestIAssets.iAssetsExtension == it.extension }.map { it.toString() }
        assertEquals(initAssetPaths, assetFiles.toSet())
    }

    @Test
    fun testClearAssets() {

        loadAssetsByLocation<TestIAsset>(TestIAssets.iAssetsLocation, TestIAssets.iAssetsExtension)

        assertEquals(3, getAssets<TestIAsset>().size)

        AssetManagerHandler.clearAssets<TestIAsset>()

        assertEquals(0, getAssets<TestIAsset>().size)
    }

    @Test
    fun testLoadAssetsFromLocation() {

        val initAssetIds = testData.map { it.assetId() }.toSet()

        assertEquals(0, getAssets<TestIAsset>().size)

        loadAssetsByLocation<TestIAsset>(TestIAssets.iAssetsLocation, TestIAssets.iAssetsExtension)

        assertEquals(3, getAssets<TestIAsset>().size)

        val loadedAssetIds = getAssets<TestIAsset>().map { it.assetId() }.toSet()

        assertEquals(initAssetIds, loadedAssetIds)
    }

    @Test
    fun testLoadAssetsFromPath() {

        assertEquals(0, getAssets<TestIAsset>().size)

        val paramTestString = "paramTestStrings"

        val testParamList = List(testData.size) { idx -> TestIAssetLoader.TestIAssetLoaderParameter().apply { this.testString = "$paramTestString$idx" }}

        testParamList.forEach { Log.test("using params", it.testString) }

        loadAssetsByPath<TestIAsset>(testData.sortedBy { it.assetPath() }.map { it.assetPath() }, testParamList )

        assertEquals(3, getAssets<TestIAsset>().size)

        val loadedAssets = getAssets<TestIAsset>()

        loadedAssets.sortedBy { it.assetPath() }.forEachIndexed { idx, it -> assertEquals("$paramTestString$idx", it.assetStatus?.statusDetail) }
    }

    @Test
    fun testLoadAssetFromPath() {

        assertEquals(0, getAssets<TestIAsset>().size)

        val paramTestString = "paramTestString123"

        val testParam = TestIAssetLoader.TestIAssetLoaderParameter().apply { this.testString = paramTestString }

        Log.test("using param", testParam.testString)

        loadAssetByPath<TestIAsset>(testData[0].assetPath(), testParam)

        assertEquals(1, getAssets<TestIAsset>().size)

        val loadedAsset = getAsset<TestIAsset>(testData[0].assetPath())

        assertEquals(paramTestString, loadedAsset.assetStatus?.statusDetail)
    }

    @Test
    fun testLogAssets() {

        loadAssetsByLocation<TestIAsset>(TestIAssets.iAssetsLocation, TestIAssets.iAssetsExtension)

        getAssets<TestIAsset>()[0].assetStatus = AssetStatus("assetPath", AssetStatus.AssetNotLoaded, "test_status_detail")

        val failedAssets = logAssets<TestIAsset>()

        assertEquals(1, failedAssets.size)
    }

    fun persistTestIAssetStore() {

        TestIAssetStore.values().forEach {
            Log.test("persisting", it.path)
            val persistAsset = TestIAsset(it.path).apply { this.testIAssetData.dataString = it.path }
            AssetManagerHandler.safeSave<TestIAsset.TestIAssetData>(persistAsset, it.path)
        }
    }


    fun cleanupTestIAssetStore() {

        TestIAssetStore.values().forEach {
            Path(it.path).deleteIfExists()
            Log.test("deleted", it.path)
        }
    }

    @Test
    fun testIAssetStore() {
        persistTestIAssetStore()

        TestIAssetStore.loadAll()

        val loadedVals = TestIAssetStore.getAll()

        Log.test("loadedVals", loadedVals.map { it.testIAssetData.dataString }.str(", "))

        TestIAssetStore.values().forEachIndexed { idx, it -> assertEquals(it.path, loadedVals[idx].testIAssetData.dataString) }

        cleanupTestIAssetStore()
    }

    @Test
    fun testNoLoadGetException() {

        Log.test("attempting to getAsset without load", TestIAssetStore.TestIAsset0.path)

        val getAssetNoLoadException = Assertions.assertThrows(GdxRuntimeException::class.java) { TestIAssetStore.TestIAsset0.get() }
        Log.test("getAsset() exception correctly thrown for unloaded asset", getAssetNoLoadException.message)


        Log.test("attempting to getAssets without load", TestIAssetStore.values().map { it.path }.str(","))

        val getAssetsNoLoadException = Assertions.assertThrows(GdxRuntimeException::class.java) { TestIAssetStore.getAll() }
        Log.test("getAssets() exception correctly thrown for unloaded asset", getAssetsNoLoadException.message)

    }
}