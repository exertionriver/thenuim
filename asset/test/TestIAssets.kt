import org.junit.jupiter.api.Test
import river.exertion.kcop.asset.IAsset
import river.exertion.kcop.asset.IAssets

class TestIAssets : IAssets {

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

    //not used
    override val iAssetLocation: String = "kcop/TestIAssets"
    override val iAssetExtension: String = ".json"

    @Test
    fun testReload() {

        println("init values:")
        values.forEach { println("assetId: ${it.assetId}") }
        assert(values.size == 0)

        reload()

        println("loaded values:")
        values.forEach { println("assetId: ${it.assetId}")}
        assert(values.size == 3)
    }

    @Test
    fun testById() {
        reload()

        val testAsset = values[0]
        val byIdAsset = byId(testAsset.assetId)

        assert(testAsset.assetName == byIdAsset?.assetName)
    }

    @Test
    fun testByName() {
        reload()

        val testAsset = values[1]
        val byNameAsset = byName(testAsset.assetName)

        assert(testAsset.assetTitle == byNameAsset?.assetTitle)
    }

    @Test
    fun testByTitle() {
        reload()

        val testAsset = values[2]
        val byTitleAsset = byTitle(testAsset.assetTitle)

        assert(testAsset.assetId == byTitleAsset?.assetId)
    }

    @Test
    fun testGet() {
        reload()

        val getAssets = get()
        val getTestIAssets = super.getTyped<TestIAsset>()
        val getIAssets = super.getTyped<IAsset>()

        assert(getAssets.size == 3)
        assert(getTestIAssets.size == 3)
        assert(getIAssets.size == 3)
    }

}
