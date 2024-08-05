package river.exertion.thenuim.view.asset

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import river.exertion.thenuim.asset.AssetManagerHandler
import river.exertion.thenuim.asset.IAsset
import river.exertion.thenuim.asset.IAssets
import kotlin.io.path.*

object FreeTypeFontAssets : IAssets {

    override var values: MutableList<IAsset> = mutableListOf()

    override fun byId(assetId : String?) : FreeTypeFontAsset? = super.byIdTyped(assetId)

    override fun byName(assetName : String?) : FreeTypeFontAsset? = super.byNameTyped(assetName)

    override fun byTitle(assetTitle : String?) : FreeTypeFontAsset? = super.byTitleTyped(assetTitle)

    override fun <T: IAsset>reloadTyped() : MutableList<T> {
        //external fonts
        AssetManagerHandler.assets.setLoader(FreeTypeFontGenerator::class.java, FreeTypeFontGeneratorLoader(
            AssetManagerHandler.lfhr))
        AssetManagerHandler.assets.setLoader(BitmapFont::class.java, ".ttf", FreetypeFontLoader(AssetManagerHandler.lfhr))
        AssetManagerHandler.assets.setLoader(String::class.java, ".txt", StringLoader(AssetManagerHandler.lfhr))

        values.clear()

        if (Path(iAssetsLocation).exists()) {
            Path(iAssetsLocation).listDirectoryEntries().filter { iAssetsExtension == it.extension }.forEach {
                val charExtensionFilename = it.pathString.substring(0, it.pathString.length - 4) + "_ext.txt"

                AssetManagerHandler.loadAssetByPath<String>(charExtensionFilename)

                val charExtensions = AssetManagerHandler.getAsset<String>(charExtensionFilename)

                AssetManagerHandler.loadAssetByPath(it.pathString, FreeTypeFontAssetStore.ftflp().apply {
                    this.fontFileName = it.pathString
                    this.fontParameters.characters += charExtensions
                })
            }

            val bitmapFonts = AssetManagerHandler.getAssets<BitmapFont>()

            bitmapFonts.forEach { values.add(FreeTypeFontAsset(it)) }
        }


        return getTyped()
    }

    override fun reload() : MutableList<FreeTypeFontAsset> = reloadTyped()

    override fun get() : MutableList<FreeTypeFontAsset> = super.getTyped()

    override val iAssetsLocation = "assetExt/freeTypeFont/"
    override val iAssetsExtension = "ttf"
}