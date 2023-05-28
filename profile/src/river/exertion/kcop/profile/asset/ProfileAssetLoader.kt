package river.exertion.kcop.profile.asset

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.files.FileHandle
import kotlinx.serialization.json.decodeFromJsonElement
import river.exertion.kcop.asset.AssetManagerHandler.json
import river.exertion.kcop.profile.Profile

class ProfileAssetLoader(resolver: FileHandleResolver?) :
    AsynchronousAssetLoader<ProfileAsset?, ProfileAssetLoader.ProfileParameter?>(resolver) {

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
                this.persisted = true
            }

        } catch (ex : Exception) {
            ProfileAsset().apply {
                this.assetPath = fileName
                this.status = "$fileName not loaded"
                this.statusDetail = ex.message
            }
        }
    }

    class ProfileParameter : AssetLoaderParameters<ProfileAsset?>()
}