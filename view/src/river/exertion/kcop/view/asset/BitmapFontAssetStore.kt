package river.exertion.kcop.view.asset

import com.badlogic.gdx.assets.loaders.BitmapFontLoader
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import river.exertion.kcop.asset.AssetManagerHandler
import river.exertion.kcop.asset.IAssetStore
import river.exertion.kcop.asset.IAssetStoreCompanion
import river.exertion.kcop.view.KcopFont

//Used for kcop-internal bitmap fonts in a known location
enum class BitmapFontAssetStore(val path: String) : IAssetStore {
    OpenSansRegular("fonts/openSans/OSR.fnt")
    ;

    override fun load() = AssetManagerHandler.loadAssetByPath(path, bfp)
    override fun get() = AssetManagerHandler.getAsset<BitmapFont>(path).apply { this.data.setScale(KcopFont.TEXT.fontScale)}

    open fun bfp() = bfp

    companion object : IAssetStoreCompanion {
        override fun loadAll() = AssetManagerHandler.loadAssetsByPath<BitmapFont>(values().map { it.path }, values().map { it.bfp() } )
        override fun getAll() = AssetManagerHandler.getAssets<BitmapFont>(values().map { it.path })

        val bfp = BitmapFontLoader.BitmapFontParameter().apply {
            this.genMipMaps = true
            this.minFilter = Texture.TextureFilter.Linear
            this.magFilter = Texture.TextureFilter.Linear
        }
    }
}