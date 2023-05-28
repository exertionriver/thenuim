package river.exertion.kcop.asset

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver
import kotlinx.serialization.json.Json
import ktx.assets.disposeSafely
import ktx.assets.load
import ktx.assets.unloadSafely
import ktx.collections.gdxArrayOf
import river.exertion.kcop.messaging.Id.Companion.logDebug
import kotlin.io.path.Path
import kotlin.io.path.listDirectoryEntries

object AssetManagerHandler {

    val assets = AssetManager()
    val lfhr = LocalFileHandleResolver()
    val ifhr = InternalFileHandleResolver()

    val json = Json { ignoreUnknownKeys = true; encodeDefaults = true; isLenient = true }

    inline fun <reified T: IAsset>reloadLocalAssets(assetLoadLocation : String): List<T> {

        //remove previous assets of type T
        val previousAssetArray = gdxArrayOf<T>()

        assets.getAll(T::class.java, previousAssetArray)
        previousAssetArray.forEach {
            if (it.assetPath != null) assets.unloadSafely(it.assetPath!!)
        }

        //TODO: exception handling for path creation
        //reload assets of type T
        Path(assetLoadLocation).listDirectoryEntries().forEach {
            assets.load<T>(it.toString())
        }
        assets.finishLoading()

        val currentAssetArray = gdxArrayOf<T>()
        assets.getAll(T::class.java, currentAssetArray)

        //log any load errors
        currentAssetArray.filter { it.status != null }.forEach {
            logDebug("${it.status}", "${it.statusDetail}")
        }

        return currentAssetArray.toMutableList()
    }

    fun dispose() {
        assets.disposeSafely()
    }

}