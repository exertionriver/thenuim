package river.exertion.kcop.asset

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver
import com.badlogic.gdx.files.FileHandle
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import ktx.assets.disposeSafely
import ktx.assets.load
import ktx.assets.unloadSafely
import ktx.collections.gdxArrayOf
import kotlin.io.path.Path
import kotlin.io.path.listDirectoryEntries

object AssetManagerHandler {

    val assets = AssetManager()
    val lfhr = LocalFileHandleResolver()
    val ifhr = InternalFileHandleResolver()

    val json = Json { ignoreUnknownKeys = true; encodeDefaults = true; isLenient = true }

    inline fun <reified T: IAsset>reloadLocalAssets(assetLoadLocation : String): List<T> {

        clearAssets<T>()
        loadAssets<T>(assetLoadLocation)
        return logAssets<T>()
    }

    inline fun <reified T: IAsset>clearAssets() {

        val previousAssetArray = gdxArrayOf<T>()
        assets.getAll(T::class.java, previousAssetArray)

        previousAssetArray.forEach {
            if (it.assetPath != null) assets.unloadSafely(it.assetPath!!)
        }
    }

    inline fun <reified T: IAsset>loadAssets(assetLoadLocation : String) {

        Path(assetLoadLocation).listDirectoryEntries().forEach {
            assets.load<T>(it.toString())
        }
        assets.finishLoading()
    }

    inline fun <reified T: IAsset>logAssets() : List<T> {

        val currentAssetArray = gdxArrayOf<T>()
        assets.getAll(T::class.java, currentAssetArray)

        //log any load errors
        currentAssetArray.filter { it.status != null }.forEach {
            logDebug("${it.status}", "${it.statusDetail}")
        }

        return currentAssetArray.toList()
    }

    inline fun <reified T: Any>saveAsset(asset : IAsset, assetSaveLocation : String? = null) : IAsset {
        val jsonAssetData = json.encodeToJsonElement(asset.assetData() as T)

        val localFileHandle = if (Gdx.files != null)
            Gdx.files.local(asset.assetPath)
        else
            FileHandle(Path(asset.assetPath!!).toString())

        localFileHandle.writeString(jsonAssetData.toString(), false)

        if (assetSaveLocation != null && assetSaveLocation != asset.assetPath) {
            val moveToFileHandle = if (Gdx.files != null)
                Gdx.files.local(assetSaveLocation)
            else
                FileHandle(Path(assetSaveLocation).toString())

            localFileHandle.moveTo(moveToFileHandle)
        }

        return asset.apply { this.persisted = true }
    }

    fun dispose() {
        assets.disposeSafely()
    }

    fun logDebug(tag : String, message : String) : String {
        val returnLog = "$tag: $message"

        if (Gdx.app != null)
            Gdx.app.debug(tag, message)
        else
            println(returnLog)

        return returnLog
    }
}