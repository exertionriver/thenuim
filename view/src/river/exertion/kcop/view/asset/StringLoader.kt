package river.exertion.kcop.view.asset

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.files.FileHandle

class StringLoader(resolver: FileHandleResolver?) :
    AsynchronousAssetLoader<String?, StringLoader.NarrativeSequenceParameter?>(resolver) {

    override fun getDependencies(fileName: String?, file: FileHandle?, parameter: NarrativeSequenceParameter?): com.badlogic.gdx.utils.Array<AssetDescriptor<Any>>? {
        return null
    }

    override fun loadAsync(manager: AssetManager, fileName: String, file: FileHandle, parameter: NarrativeSequenceParameter?) {
    }

    override fun loadSync(manager: AssetManager, fileName: String, file: FileHandle, parameter: NarrativeSequenceParameter?): String {
        return try {
            file.readString()
        } catch (ex: Exception) {
            ex.message!!
        }
        println("")
    }

    class NarrativeSequenceParameter : AssetLoaderParameters<String?>()
    }