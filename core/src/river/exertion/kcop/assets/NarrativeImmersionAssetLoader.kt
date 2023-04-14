package river.exertion.kcop.assets

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.files.FileHandle
import kotlinx.serialization.json.decodeFromJsonElement
import river.exertion.kcop.narrative.structure.NarrativeImmersion

class NarrativeImmersionAssetLoader(resolver: FileHandleResolver?) :
    AsynchronousAssetLoader<NarrativeImmersionAsset?, NarrativeImmersionAssetLoader.NarrativeImmersionSequenceParameter?>(resolver) {

    lateinit var rawData: String

    override fun getDependencies(fileName: String?, file: FileHandle?, parameter: NarrativeImmersionSequenceParameter?): com.badlogic.gdx.utils.Array<AssetDescriptor<Any>>? {
        return null
    }

    override fun loadAsync(manager: AssetManager, fileName: String, file: FileHandle, parameter: NarrativeImmersionSequenceParameter?) {
    }

    override fun loadSync(manager: AssetManager, fileName: String, file: FileHandle, parameter: NarrativeImmersionSequenceParameter?): NarrativeImmersionAsset {
        try {
            rawData = file.readString()
            val jsonElement = AssetManagerHandler.json.parseToJsonElement(rawData)
            val narrativeImmersion = AssetManagerHandler.json.decodeFromJsonElement(jsonElement) as NarrativeImmersion

            val returnNarrativeAsset = NarrativeImmersionAsset(narrativeImmersion).apply { this.assetPath = fileName }
            //val errorStatus = "${narrativeImmersion.id} not loaded"

            return returnNarrativeAsset

        } catch (ex : Exception) {
            return NarrativeImmersionAsset().apply {
                this.assetPath = fileName
                this.status = "$fileName not loaded"
                this.statusDetail = ex.message
            }
        }
    }

    class NarrativeImmersionSequenceParameter : AssetLoaderParameters<NarrativeImmersionAsset?>()
}