package river.exertion.thenuim.asset

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import ktx.assets.disposeSafely
import ktx.assets.getAsset
import ktx.assets.load
import ktx.assets.unloadSafely
import ktx.collections.gdxArrayOf
import river.exertion.thenuim.base.Log
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.extension
import kotlin.io.path.listDirectoryEntries

object AssetManagerHandler {

    val assets = AssetManager()
    val lfhr = LocalFileHandleResolver()
    val ifhr = InternalFileHandleResolver()

    val json = Json { ignoreUnknownKeys = true; encodeDefaults = true; isLenient = true }

    fun loadedSize() = assets.loadedAssets

    inline fun <reified T: IAsset>reloadLocalAssets(assetLoadLocation : String, assetLoadExtension : String): List<T> {

        clearAssets<T>()
        loadAssetsByLocation<T>(assetLoadLocation, assetLoadExtension)
        logAssets<T>()

        return getAssets<T>()
    }

    inline fun <reified T: IAsset>clearAssets() {

        getAssets<T>().forEach {
            assets.unloadSafely(it.assetPath())
        }
    }

    inline fun <reified T: IAsset>loadAssetsByLocation(assetLoadLocation : String, assetLoadExtension : String) {

        try {
            Path(assetLoadLocation).listDirectoryEntries().filter { assetLoadExtension == it.extension }.forEach {
                assets.load<T>(it.toString())
            }
            assets.finishLoading()
        } catch (ex : Exception) {
            println(ex)
        }
    }

    inline fun <reified T: Any>loadAssetsByPath(assetLoadPaths : List<String>, params: List<AssetLoaderParameters<T>?>? = null) {

        assetLoadPaths.forEachIndexed { idx, assetPath ->
            assets.load<T>(assetPath, params?.get(idx))
        }
        assets.finishLoading()
    }

    inline fun <reified T: Any>loadAssetByPath(assetLoadPath : String, param: AssetLoaderParameters<T>? = null) {
        assets.load<T>(assetLoadPath, param)
        assets.finishLoading()
    }

    inline fun <reified T: IAsset>logAssets() : MutableList<AssetStatus> {

        val returnList = mutableListOf<AssetStatus>()

        //log any load errors
        getAssets<T>().filter { it.assetStatus?.status == AssetStatus.AssetNotLoaded }.forEach {
            returnList.add(it.assetStatus!!)
        }

        returnList.forEach {
            Log.debug(it.assetPath, "${it.status}: ${it.statusDetail}")
        }

        return returnList
    }

    inline fun <reified T: Any>safeSave(asset : IAsset, assetSavePath : String) : IAsset {

        if (Path(assetSavePath).exists()) {
            throw Exception ("safeSave() : $assetSavePath already exists!")
        }

        return saveAsset<T>(asset, assetSavePath)
    }

    inline fun <reified T: Any>saveAsset(asset : IAsset, assetSavePath : String? = null) : IAsset {
        val jsonAssetData = json.encodeToJsonElement(asset.assetData() as T)

        Gdx.files.local(asset.assetPath()).writeString(jsonAssetData.toString(), false)

        if (assetSavePath != null && assetSavePath != asset.assetPath()) {
            Gdx.files.local(asset.assetPath()).moveTo(Gdx.files.local(assetSavePath))
        }

        return asset.apply { this.persisted = true }
    }

    inline fun <reified T: Any>getAssets() : List<T> {
        val assetArray = gdxArrayOf<T>()
        assets.getAll(T::class.java, assetArray)

        return assetArray.toList()
    }

    inline fun <reified T: Any>getAssets(assetLoadPaths : List<String>) : List<T> = assetLoadPaths.map { getAsset<T>(it) }

    inline fun <reified T: Any>getAsset(assetLoadPath : String) : T = assets.getAsset<T>(assetLoadPath)

    fun dispose() {
        assets.disposeSafely()
    }
}