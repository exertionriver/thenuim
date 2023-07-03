import river.exertion.kcop.asset.AssetManagerHandler
import river.exertion.kcop.asset.IAssetStore
import river.exertion.kcop.asset.IAssetStoreCompanion

enum class TestIAssetStore(val path: String) : IAssetStore {
    TestIAsset0("testIAsset0.json"),
    TestIAsset1("testIAsset1.json"),
    TestIAsset2("testIAsset2.json"),
    ;
    override fun load() = AssetManagerHandler.loadAssetByPath<TestIAsset>(path)
    override fun get() = AssetManagerHandler.getAsset<TestIAsset>(path)

    companion object : IAssetStoreCompanion {
        override fun loadAll() = AssetManagerHandler.loadAssetsByPath<TestIAsset>(values().map { it.path })
        override fun getAll() = AssetManagerHandler.getAssets<TestIAsset>(values().map { it.path })
    }
}