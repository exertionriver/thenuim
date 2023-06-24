import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import river.exertion.kcop.asset.IAsset
import river.exertion.kcop.asset.IAssets
import river.exertion.kcop.base.GdxTestBase
import river.exertion.kcop.base.Log
import river.exertion.kcop.base.TestBase

class TestIAssets : IAssets, TestBase() {

    override var values: MutableList<IAsset> = mutableListOf()

    override fun byId(assetId: String?): TestIAsset? = super.byIdTyped(assetId)
    override fun byName(assetName: String?): TestIAsset? = super.byNameTyped(assetName)
    override fun byTitle(assetTitle: String?): TestIAsset? = super.byTitleTyped(assetTitle)
    override fun <T:IAsset>reloadTyped() : MutableList<T> {
        values = mutableListOf(TestIAsset(), TestIAsset(), TestIAsset())
        return getTyped()
    }
    override fun reload() : MutableList<TestIAsset> = reloadTyped()
    override fun get(): MutableList<TestIAsset> = super.getTyped()

    override val iAssetsLocation: String = Companion.iAssetsLocation
    override val iAssetsExtension: String = Companion.iAssetsExtension

    companion object {
        val iAssetsLocation: String = ""
        val iAssetsExtension: String = "json"
        fun iAssetPath(iAssetFilename : String) = "$iAssetsLocation$iAssetFilename.$iAssetsExtension"
    }

    @AfterEach
    fun clearValues() {
        values.clear()
    }

    @Test
    fun testReload() {

        Log.test("init values")
        values.forEach { Log.test("assetId", it.assetId()) }
        assertEquals(0, values.size)

        reload()

        Log.test("loaded values")
        values.forEach { Log.test("assetId", it.assetId()) }
        assertEquals(3, values.size)
    }

    @Test
    fun testById() {
        reload()

        val testAsset = values[0]
        val byIdAsset = byId(testAsset.assetId())

        assertEquals(testAsset.assetName(), byIdAsset?.assetName())
    }

    @Test
    fun testByName() {
        reload()

        val testAsset = values[1]
        val byNameAsset = byName(testAsset.assetName())

        assertEquals(testAsset.assetTitle(), byNameAsset?.assetTitle())
    }

    @Test
    fun testByTitle() {
        reload()

        val testAsset = values[2]
        val byTitleAsset = byTitle(testAsset.assetTitle())

        assertEquals(testAsset.assetId(), byTitleAsset?.assetId())
    }

    @Test
    fun testGet() {
        reload()

        val getAssets = get()
        val getTestIAssets = super.getTyped<TestIAsset>()
        val getIAssets = super.getTyped<IAsset>()

        assertEquals(3, getAssets.size)
        assertEquals(3, getTestIAssets.size)
        assertEquals(3, getIAssets.size)
    }

}
