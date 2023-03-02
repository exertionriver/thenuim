package river.exertion.kcop.assets

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Texture
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import river.exertion.kcop.narrative.structure.Narrative
import ktx.assets.load
import river.exertion.kcop.system.profile.Profile

class ProfileAssetLoader(resolver: FileHandleResolver?) :
    AsynchronousAssetLoader<ProfileAsset?, ProfileAssetLoader.ProfileParameter?>(resolver) {

    val json = Json { ignoreUnknownKeys = true }
    lateinit var rawData: String

    override fun getDependencies(fileName: String?, file: FileHandle?, parameter: ProfileParameter?): com.badlogic.gdx.utils.Array<AssetDescriptor<Any>>? {
        return null
    }

    override fun loadAsync(manager: AssetManager, fileName: String, file: FileHandle, parameter: ProfileParameter?) {
    }

    override fun loadSync(manager: AssetManager, fileName: String, file: FileHandle, parameter: ProfileParameter?): ProfileAsset {
        return try {
            rawData = file.readString()
            val jsonElement = json.parseToJsonElement(rawData)
            val profile = json.decodeFromJsonElement(jsonElement) as Profile

            ProfileAsset(profile).apply {
                this.assetPath = fileName
            }

        } catch (ex : Exception) {
            ProfileAsset().apply {
                this.status = "not loaded"
                this.statusDetail = ex.message
                this.assetPath = fileName
            }
        }
    }

    class ProfileParameter : AssetLoaderParameters<ProfileAsset?>()
}