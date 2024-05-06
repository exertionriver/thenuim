package river.exertion.thenuim.profile.asset

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.files.FileHandle
import kotlinx.serialization.json.decodeFromJsonElement
import river.exertion.thenuim.asset.AssetManagerHandler.json
import river.exertion.thenuim.asset.AssetStatus
import river.exertion.thenuim.profile.Profile

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
                this.persisted = true
            }

        } catch (ex : Exception) {
            ProfileAsset().apply {
                this.assetStatus = AssetStatus(this.assetPath(), "asset not loaded", ex.message)
            }
        }
    }

    class ProfileParameter : AssetLoaderParameters<ProfileAsset?>()
}