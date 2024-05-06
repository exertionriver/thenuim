package river.exertion.thenuim.sim.narrative.asset

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.files.FileHandle
import kotlinx.serialization.json.decodeFromJsonElement
import river.exertion.thenuim.asset.AssetManagerHandler.json
import river.exertion.thenuim.asset.AssetStatus
import river.exertion.thenuim.sim.narrative.structure.NarrativeState

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

            val returnNarrativeStateAsset = NarrativeStateAsset(narrativeState)
            //val errorStatus = "${narrativeImmersion.id} not loaded"

            returnNarrativeStateAsset.persisted = true

            return returnNarrativeStateAsset

        } catch (ex : Exception) {
            return NarrativeStateAsset().apply {
                this.assetStatus = AssetStatus(this.assetPath(), "asset not loaded", ex.message)
            }
        }
    }

    class NarrativeImmersionSequenceParameter : AssetLoaderParameters<NarrativeStateAsset?>()
}