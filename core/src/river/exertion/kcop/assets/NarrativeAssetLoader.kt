package river.exertion.kcop.assets

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.files.FileHandle
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import river.exertion.kcop.narrative.structure.Narrative
import river.exertion.kcop.Util

class NarrativeAssetLoader(resolver: FileHandleResolver?) :
    AsynchronousAssetLoader<Narrative?, NarrativeAssetLoader.NarrativeSequenceParameter?>(resolver) {

    val json = Json { ignoreUnknownKeys = true }
    lateinit var rawData: String

    override fun getDependencies(fileName: String?, file: FileHandle?, parameter: NarrativeSequenceParameter?): com.badlogic.gdx.utils.Array<AssetDescriptor<Any>>? {
        return null
    }

    override fun loadAsync(manager: AssetManager, fileName: String, file: FileHandle, parameter: NarrativeSequenceParameter?) {
    }

    override fun loadSync(manager: AssetManager, fileName: String, file: FileHandle, parameter: NarrativeSequenceParameter?): Narrative? {
        try {
            rawData = file.readString()
            val jsonElement = json.parseToJsonElement(rawData)
            val narrative = json.decodeFromJsonElement(jsonElement) as Narrative
            if (parameter == null || parameter.init) narrative.init()
            return narrative
        } catch (ex : Exception) {
            Util.logDebug("loader", ex.toString())
        }
        return null
    }

    class NarrativeSequenceParameter : AssetLoaderParameters<Narrative?>() {
        /** initializes narrative sequence by default */
        var init = true
    }
}