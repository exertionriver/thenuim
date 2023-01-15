package river.exertion.kcop.assets

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont.BitmapFontData
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import river.exertion.kcop.NarrativeSequence
import river.exertion.kcop.Util

class NarrativeSequenceLoader(resolver: FileHandleResolver?) :
    AsynchronousAssetLoader<NarrativeSequence?, NarrativeSequenceLoader.NarrativeSequenceParameter?>(resolver) {

    val json = Json { ignoreUnknownKeys = true }
    var narrativeSequence: NarrativeSequence? = null

    override fun getDependencies(fileName: String?, file: FileHandle?, parameter: NarrativeSequenceParameter?): com.badlogic.gdx.utils.Array<AssetDescriptor<Any>>? {
        return null
    }

    override fun loadAsync(manager: AssetManager, fileName: String, file: FileHandle, parameter: NarrativeSequenceParameter?) {
    }

    override fun loadSync(manager: AssetManager, fileName: String, file: FileHandle, parameter: NarrativeSequenceParameter?): NarrativeSequence? {
        try {
            val jsonElement = json.parseToJsonElement(file.readString())
            val narrativeSequence = json.decodeFromJsonElement(jsonElement) as NarrativeSequence
            if (parameter != null && !parameter.init) {} else narrativeSequence.init()
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