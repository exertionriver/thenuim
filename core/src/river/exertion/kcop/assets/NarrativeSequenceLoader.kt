package river.exertion.kcop.assets

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.files.FileHandle
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import river.exertion.kcop.narrative.sequence.NarrativeSequence
import river.exertion.kcop.Util

class NarrativeSequenceLoader(resolver: FileHandleResolver?) :
    AsynchronousAssetLoader<NarrativeSequence?, NarrativeSequenceLoader.NarrativeSequenceParameter?>(resolver) {

    val json = Json { ignoreUnknownKeys = true }
    lateinit var rawData: String

    override fun getDependencies(fileName: String?, file: FileHandle?, parameter: NarrativeSequenceParameter?): com.badlogic.gdx.utils.Array<AssetDescriptor<Any>>? {
        return null
    }

    override fun loadAsync(manager: AssetManager, fileName: String, file: FileHandle, parameter: NarrativeSequenceParameter?) {
    }

    override fun loadSync(manager: AssetManager, fileName: String, file: FileHandle, parameter: NarrativeSequenceParameter?): NarrativeSequence? {
        try {
            rawData = file.readString()
            val jsonElement = json.parseToJsonElement(rawData)
            val narrativeSequence = json.decodeFromJsonElement(jsonElement) as NarrativeSequence
            if (parameter == null || parameter.init) narrativeSequence.init()
            return narrativeSequence
        } catch (ex : Exception) {
            Util.logDebug("loader", ex.toString())
        }
        return null
    }

    class NarrativeSequenceParameter : AssetLoaderParameters<NarrativeSequence?>() {
        /** initializes narrative sequence by default */
        var init = true
    }
}