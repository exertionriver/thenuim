package river.exertion.kcop.sim.narrative.asset

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.files.FileHandle
import kotlinx.serialization.json.decodeFromJsonElement
import river.exertion.kcop.asset.AssetManagerHandler.json
import river.exertion.kcop.sim.narrative.structure.NarrativeState

class NarrativeStateAssetLoader(resolver: FileHandleResolver?) :
    AsynchronousAssetLoader<NarrativeStateAsset?, NarrativeStateAssetLoader.NarrativeImmersionSequenceParameter?>(resolver) {

    lateinit var rawData: String

    override fun getDependencies(fileName: String?, file: FileHandle?, parameter: NarrativeImmersionSequenceParameter?): com.badlogic.gdx.utils.Array<AssetDescriptor<Any>>? {
        return null
    }

    override fun loadAsync(manager: AssetManager, fileName: String, file: FileHandle, parameter: NarrativeImmersionSequenceParameter?) {
    }

    override fun loadSync(manager: AssetManager, fileName: String, file: FileHandle, parameter: NarrativeImmersionSequenceParameter?): NarrativeStateAsset {
        try {
            rawData = file.readString()
            val jsonElement = json.parseToJsonElement(rawData)
            val narrativeState = json.decodeFromJsonElement(jsonElement) as NarrativeState

            val returnNarrativeAsset = NarrativeStateAsset(narrativeState).apply { this.assetPath = fileName }
            //val errorStatus = "${narrativeImmersion.id} not loaded"

            return returnNarrativeAsset

        } catch (ex : Exception) {
            return NarrativeStateAsset().apply {
                this.assetPath = fileName
                this.status = "$fileName not loaded"
                this.statusDetail = ex.message
            }
        }
    }

    class NarrativeImmersionSequenceParameter : AssetLoaderParameters<NarrativeStateAsset?>()
}