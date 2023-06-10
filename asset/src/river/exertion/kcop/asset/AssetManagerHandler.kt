package river.exertion.kcop.asset

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import ktx.assets.disposeSafely
import ktx.assets.load
import ktx.assets.unloadSafely
import ktx.collections.gdxArrayOf
import kotlin.io.path.Path
import kotlin.io.path.extension
import kotlin.io.path.listDirectoryEntries

object AssetManagerHandler {

    val assets = AssetManager()
    val lfhr = LocalFileHandleResolver()
    val ifhr = InternalFileHandleResolver()

    val json = Json { ignoreUnknownKeys = true; encodeDefaults = true; isLenient = true }

    inline fun <reified T: IAsset>reloadLocalAssets(assetLoadLocation : String, assetLoadExtension : String): List<T> {

        clearAssets<T>()
        loadAssets<T>(assetLoadLocation, assetLoadExtension)
        logAssets<T>()

        return getAssets<T>()
    }

    inline fun <reified T: IAsset>clearAssets() {

        getAssets<T>().forEach {
            assets.unloadSafely(it.assetPath())
        }
    }

    inline fun <reified T: IAsset>loadAssets(assetLoadLocation : String, assetLoadExtension : String) {

        Path(assetLoadLocation).listDirectoryEntries().filter { assetLoadExtension == it.extension }.forEach {
            assets.load<T>(it.toString())
        }
        assets.finishLoading()
    }

    inline fun <reified T: IAsset>logAssets() : MutableList<AssetStatus> {

        val returnList = mutableListOf<AssetStatus>()

        //log any load errors
        getAssets<T>().filter { it.assetStatus != null }.forEach {
            returnList.add(it.assetStatus!!)
        }

        returnList.forEach {
            logDebug(it.assetPath, "${it.status}: ${it.statusDetail}")
        }

        return returnList
    }

    inline fun <reified T: Any>saveAsset(asset : IAsset, assetSaveLocation : String? = null) : IAsset {
        val jsonAssetData = json.encodeToJsonElement(asset.assetData() as T)

        Gdx.files.local(asset.assetPath()).writeString(jsonAssetData.toString(), false)

        if (assetSaveLocation != null && assetSaveLocation != asset.assetPath()) {

            Gdx.files.local(asset.assetPath()).moveTo(Gdx.files.local(assetSaveLocation))
        }

        return asset.apply { this.persisted = true }
    }

    inline fun <reified T: Any>getAssets() : List<T> {
        val assetArray = gdxArrayOf<T>()
        assets.getAll(T::class.java, assetArray)

        return assetArray.toList()
    }

    fun dispose() {
        assets.disposeSafely()
    }

    fun logDebug(tag : String, message : String) : String {
        val returnLog = "$tag: $message"

        if (Gdx.app != null)
            Gdx.app.debug(tag, message)
        else //for non-Gdx unit tests
            println(returnLog)

        return returnLog
    }
}