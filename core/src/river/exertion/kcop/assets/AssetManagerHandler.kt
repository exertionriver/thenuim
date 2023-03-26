package river.exertion.kcop.assets

import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.ai.msg.Telegraph
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import ktx.assets.load
import java.nio.file.Path
import kotlin.io.path.listDirectoryEntries

class AssetManagerHandler : Telegraph {

    val assets = AssetManager()
    val lfhr = LocalFileHandleResolver()
    val ifhr = InternalFileHandleResolver()

    init {
        assets.setLoader(ProfileAsset::class.java, ProfileAssetLoader(lfhr))
        assets.setLoader(NarrativeAsset::class.java, NarrativeAssetLoader(lfhr))
        assets.setLoader(FreeTypeFontGenerator::class.java, FreeTypeFontGeneratorLoader(ifhr))
        assets.setLoader(BitmapFont::class.java, ".ttf", FreetypeFontLoader(ifhr))
    }

    //    override val assetLoadPath = Path(NarrativeAssets.narrativeAssetLocation)

    inline fun <reified T:LoadableAsset>loadAssets(assetLoadPath : Path): Map<String, T> {

        val returnAssets = mutableMapOf<String, T>()

        assetLoadPath.listDirectoryEntries().forEach {
            assets.load<T>(it.toString())
        }
        assets.finishLoading()

        assets.assetNames.forEach {
            if ((assets.get(it) as T).status == null)
                returnAssets[it] = assets[it]
        }

        return returnAssets
    }

    override fun handleMessage(msg: Telegram?): Boolean {
        if (msg != null) {
            return true
        }
        return false
    }

    fun dispose() {
        assets.dispose()
    }
}