package river.exertion.kcop.view.layout.displayViewLayout.asset

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.files.FileHandle
import kotlinx.serialization.json.decodeFromJsonElement
import river.exertion.kcop.asset.AssetManagerHandler.json
import river.exertion.kcop.asset.AssetStatus
import river.exertion.kcop.view.layout.displayViewLayout.DVLayout

class DisplayViewLayoutAssetLoader(resolver: FileHandleResolver?) :
    AsynchronousAssetLoader<DisplayViewLayoutAsset?, DisplayViewLayoutAssetLoader.DisplayViewLayoutAssetParameter?>(resolver) {

    lateinit var rawData: String

    override fun getDependencies(fileName: String?, file: FileHandle?, parameter: DisplayViewLayoutAssetParameter?): com.badlogic.gdx.utils.Array<AssetDescriptor<Any>>? {
        return null
    }

    override fun loadAsync(manager: AssetManager, fileName: String, file: FileHandle, parameter: DisplayViewLayoutAssetParameter?) {
    }

    override fun loadSync(manager: AssetManager, fileName: String, file: FileHandle, parameter: DisplayViewLayoutAssetParameter?): DisplayViewLayoutAsset {

        return try {
            rawData = file.readString()
            val jsonElement = json.parseToJsonElement(rawData)
            val dvLayout = json.decodeFromJsonElement(jsonElement) as DVLayout

            DisplayViewLayoutAsset(dvLayout)

        } catch (ex : Exception) {
            DisplayViewLayoutAsset().apply {
                this.assetStatus = AssetStatus(this.assetPath(), "asset not loaded", ex.message)
            }
        }
    }

    class DisplayViewLayoutAssetParameter : AssetLoaderParameters<DisplayViewLayoutAsset?>()
    }