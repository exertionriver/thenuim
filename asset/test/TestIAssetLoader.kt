import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.files.FileHandle
import kotlinx.serialization.json.decodeFromJsonElement
import river.exertion.thenuim.asset.AssetManagerHandler.json
import river.exertion.thenuim.asset.AssetStatus

class TestIAssetLoader(resolver: FileHandleResolver?) :
    AsynchronousAssetLoader<TestIAsset, TestIAssetLoader.TestIAssetLoaderParameter>(resolver) {

    lateinit var rawData: String

    override fun getDependencies(fileName: String?, file: FileHandle?, parameter: TestIAssetLoaderParameter?): com.badlogic.gdx.utils.Array<AssetDescriptor<Any>>? {
        return null
    }

    override fun loadAsync(manager: AssetManager, fileName: String, file: FileHandle, parameter: TestIAssetLoaderParameter?) {
    }

    override fun loadSync(manager: AssetManager, fileName: String, file: FileHandle, parameter: TestIAssetLoaderParameter?): TestIAsset {
        return try {
            rawData = file.readString()
            val jsonElement = json.parseToJsonElement(rawData)
            val testIAssetData = json.decodeFromJsonElement(jsonElement) as TestIAsset.TestIAssetData

            TestIAsset().apply {
                this.testIAssetData = testIAssetData
                this.persisted = true

                this.assetStatus = AssetStatus(this.assetPath(), AssetStatus.AssetLoaded, parameter?.testString)
            }

        } catch (ex : Exception) {
            TestIAsset().apply {
                this.assetStatus = AssetStatus(this.assetPath(), AssetStatus.AssetNotLoaded, ex.message)
            }
        }
    }

    class TestIAssetLoaderParameter : AssetLoaderParameters<TestIAsset>() {
        var testString : String = ""
    }
}