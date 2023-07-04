package river.exertion.kcop.view.asset

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import river.exertion.kcop.asset.AssetManagerHandler
import river.exertion.kcop.asset.IAssetStore
import river.exertion.kcop.asset.IAssetStoreCompanion
import river.exertion.kcop.view.KcopFont

enum class FreeTypeFontAssetStore(val path: String) : IAssetStore {
    //https://fonts.google.com/noto/specimen/Noto+Sans+Symbols
    NotoSansSymbolsSemiBoldText("fonts/notoSansSymbols/static/NotoSansSymbols-SemiBold.ttf") { override fun baseFontSize() =
        KcopFont.TEXT
    },
    //https://www.1001freefonts.com/fantasy-fonts.php
    ImmortalLarge("fonts/immortal/IMMORTAL_large.ttf") { override fun baseFontSize() = KcopFont.LARGE },
    ImmortalMedium("fonts/immortal/IMMORTAL_medium.ttf") { override fun baseFontSize() = KcopFont.MEDIUM },
    ImmortalSmall("fonts/immortal/IMMORTAL_small.ttf") { override fun baseFontSize() = KcopFont.SMALL }
    ;
    abstract fun baseFontSize() : KcopFont
    open fun ftflp() = Companion.ftflp().apply { this.fontFileName = path }

    override fun load() = AssetManagerHandler.loadAssetByPath(path, ftflp())
    override fun get() = AssetManagerHandler.getAsset<BitmapFont>(path).apply { this.data.setScale(baseFontSize().fontScale()) }

    companion object : IAssetStoreCompanion {
        override fun loadAll() = AssetManagerHandler.loadAssetsByPath<BitmapFont>(values().map { it.path }, values().map { it.ftflp() } )
        override fun getAll() = AssetManagerHandler.getAssets<BitmapFont>(values().map { it.path })

        fun ftflp() = FreetypeFontLoader.FreeTypeFontLoaderParameter().apply {
            this.fontParameters.genMipMaps = true
            this.fontParameters.minFilter = Texture.TextureFilter.MipMapLinearLinear
            this.fontParameters.magFilter = Texture.TextureFilter.Linear
            this.fontParameters.characters = this.fontParameters.characters + "↑" + "↓"
            this.fontParameters.size = KcopFont.baseFontSize
        }
    }
}