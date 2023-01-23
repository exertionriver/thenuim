package river.exertion.kcop.assets

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.files.FileHandle
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import river.exertion.kcop.Util
import river.exertion.kcop.narrative.navigation.NarrativeNavigation

class NarrativeNavigationLoader(resolver: FileHandleResolver?) :
    AsynchronousAssetLoader<NarrativeNavigation?, NarrativeNavigationLoader.NarrativeNavigationParameter?>(resolver) {

    val json = Json { ignoreUnknownKeys = true }
    lateinit var rawData: String

    override fun getDependencies(fileName: String?, file: FileHandle?, parameter: NarrativeNavigationParameter?): com.badlogic.gdx.utils.Array<AssetDescriptor<Any>>? {
        return null
    }

    override fun loadAsync(manager: AssetManager, fileName: String, file: FileHandle, parameter: NarrativeNavigationParameter?) {
    }

    override fun loadSync(manager: AssetManager, fileName: String, file: FileHandle, parameter: NarrativeNavigationParameter?): NarrativeNavigation? {
        try {
            rawData = file.readString()
            val jsonElement = json.parseToJsonElement(rawData)
            val narrativeNavigation = json.decodeFromJsonElement(jsonElement) as NarrativeNavigation
            if (parameter == null || parameter.init) narrativeNavigation.init()
            return narrativeNavigation
        } catch (ex : Exception) {
            Util.logDebug("loader", ex.toString())
        }
        return null
    }

    class NarrativeNavigationParameter : AssetLoaderParameters<NarrativeNavigation?>() {
        /** initializes narrative sequence by default */
        var init = true
    }
}