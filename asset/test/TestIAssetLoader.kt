import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.files.FileHandle
import kotlinx.serialization.json.decodeFromJsonElement
import river.exertion.kcop.asset.AssetManagerHandler.json
import river.exertion.kcop.asset.AssetStatus

class TestIAssetLoader(resolver: FileHandleResolver?) :
    AsynchronousAssetLoader<TestIAsset?, TestIAssetLoader.TestIAssetParameter?>(resolver) {

    lateinit var rawData: String

    override fun getDependencies(fileName: String?, file: FileHandle?, parameter: TestIAssetParameter?): com.badlogic.gdx.utils.Array<AssetDescriptor<Any>>? {
        return null
    }

    override fun loadAsync(manager: AssetManager, fileName: String, file: FileHandle, parameter: TestIAssetParameter?) {
    }

    override fun loadSync(manager: AssetManager, fileName: String, file: FileHandle, parameter: TestIAssetParameter?): TestIAsset {
        return try {
            rawData = file.readString()
            val jsonElement = json.parseToJsonElement(rawData)
            val testIAssetData = json.decodeFromJsonElement(jsonElement) as TestIAsset.TestIAssetData

            TestIAsset().apply {
                this.testIAssetData = testIAssetData
                this.persisted = true
            }

        } catch (ex : Exception) {
            TestIAsset().apply {
                this.assetStatus = AssetStatus(this.assetPath(), "asset not loaded", ex.message)
            }
        }
    }

    class TestIAssetParameter : AssetLoaderParameters<TestIAsset?>()
}