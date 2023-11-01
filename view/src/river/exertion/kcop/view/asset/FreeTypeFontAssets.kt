package river.exertion.kcop.view.asset

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import ktx.assets.load
import river.exertion.kcop.asset.AssetManagerHandler
import river.exertion.kcop.asset.IAsset
import river.exertion.kcop.asset.IAssets
import kotlin.io.path.Path
import kotlin.io.path.extension
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.pathString

object FreeTypeFontAssets : IAssets {

    override var values: MutableList<IAsset> = mutableListOf()

    override fun byId(assetId : String?) : FreeTypeFontAsset? = super.byIdTyped(assetId)

    override fun byName(assetName : String?) : FreeTypeFontAsset? = super.byNameTyped(assetName)

    override fun byTitle(assetTitle : String?) : FreeTypeFontAsset? = super.byTitleTyped(assetTitle)

    override fun <T:IAsset>reloadTyped() : MutableList<T> {
        //external fonts
        AssetManagerHandler.assets.setLoader(FreeTypeFontGenerator::class.java, FreeTypeFontGeneratorLoader(AssetManagerHandler.lfhr))
        AssetManagerHandler.assets.setLoader(BitmapFont::class.java, ".ttf", FreetypeFontLoader(AssetManagerHandler.lfhr))

        values.clear()

        Path(iAssetsLocation).listDirectoryEntries().filter { iAssetsExtension == it.extension }.forEach {
            AssetManagerHandler.loadAssetByPath(it.pathString, FreeTypeFontAssetStore.ftflp().apply { this.fontFileName = it.pathString })
        }

        val bitmapFonts = AssetManagerHandler.getAssets<BitmapFont>()

        bitmapFonts.forEach { values.add(FreeTypeFontAsset(it)) }

        return getTyped()
    }

    override fun reload() : MutableList<FreeTypeFontAsset> = reloadTyped()

    override fun get() : MutableList<FreeTypeFontAsset> = super.getTyped()

    override val iAssetsLocation = "assetExt/freeTypeFont/"
    override val iAssetsExtension = "ttf"
}