package river.exertion.kcop.assets

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.files.FileHandle
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.json.decodeFromJsonElement

class DisplayViewLayoutAssetLoader(resolver: FileHandleResolver?) :
    AsynchronousAssetLoader<DisplayViewLayoutAsset?, DisplayViewLayoutAssetLoader.DisplayViewLayoutAssetParameter?>(resolver) {

    lateinit var rawData: String

    override fun getDependencies(fileName: String?, file: FileHandle?, parameter: DisplayViewLayoutAssetParameter?): com.badlogic.gdx.utils.Array<AssetDescriptor<Any>>? {
        return null
    }

    override fun loadAsync(manager: AssetManager, fileName: String, file: FileHandle, parameter: DisplayViewLayoutAssetParameter?) {
    }

    override fun loadSync(manager: AssetManager, fileName: String, file: FileHandle, parameter: DisplayViewLayoutAssetParameter?): DisplayViewLayoutAsset {
        try {
            rawData = file.readString()
            val jsonElement = AssetManagerHandler.json.parseToJsonElement(rawData)
            val displayViewLayout = AssetManagerHandler.json.decodeFromJsonElement(jsonElement) as DisplayViewLayout

            val returnDisplayViewLayoutAsset = DisplayViewLayoutAsset(displayViewLayout).apply { this.assetPath = fileName }
            val errorStatus = "${displayViewLayout.name} not loaded"

            return returnDisplayViewLayoutAsset

        } catch (ex : Exception) {
            return DisplayViewLayoutAsset().apply {
                this.assetPath = fileName
                this.status = "$fileName not loaded"
                this.statusDetail = ex.message
            }
        }
    }

    class DisplayViewLayoutAssetParameter : AssetLoaderParameters<DisplayViewLayoutAsset?>()
    }