package river.exertion.kcop.sim.narrative.view.asset

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.files.FileHandle
import kotlinx.serialization.json.decodeFromJsonElement
import river.exertion.kcop.asset.AssetManagerHandler.json
import river.exertion.kcop.sim.narrative.view.DVLayout

class DisplayViewLayoutAssetLoader(resolver: FileHandleResolver?) :
    AsynchronousAssetLoader<DisplayViewLayoutAsset?, DisplayViewLayoutAssetLoader.DisplayViewLayoutAssetParameter?>(resolver) {

    lateinit var rawData: String

    override fun getDependencies(fileName: String?, file: FileHandle?, parameter: DisplayViewLayoutAssetParameter?): com.badlogic.gdx.utils.Array<AssetDescriptor<Any>>? {
        return null
    }

    override fun loadAsync(manager: AssetManager, fileName: String, file: FileHandle, parameter: DisplayViewLayoutAssetParameter?) {
    }

    override fun loadSync(manager: AssetManager, fileName: String, file: FileHandle, parameter: DisplayViewLayoutAssetParameter?): DisplayViewLayoutAsset {

        lateinit var errorStatus : String

        try {
            rawData = file.readString()
            val jsonElement = json.parseToJsonElement(rawData)
            val dvLayout = json.decodeFromJsonElement(jsonElement) as DVLayout

            errorStatus = "${dvLayout.name} not loaded"

            return DisplayViewLayoutAsset(dvLayout).apply { this.assetPath = fileName }

        } catch (ex : Exception) {
            return DisplayViewLayoutAsset().apply {
                this.assetPath = fileName
                this.status = errorStatus
                this.statusDetail = ex.message
            }
        }
    }

    class DisplayViewLayoutAssetParameter : AssetLoaderParameters<DisplayViewLayoutAsset?>()
    }