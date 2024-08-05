package river.exertion.thenuim.view.asset

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import river.exertion.thenuim.asset.view.CharacterSetExtension
import river.exertion.thenuim.asset.AssetManagerHandler
import river.exertion.thenuim.asset.IAssetStore
import river.exertion.thenuim.asset.IAssetStoreCompanion
import river.exertion.thenuim.view.TnmFont

//Used for kcop-internal ttf font files
enum class FreeTypeFontAssetStore(val path: String) : IAssetStore {
    //https://fonts.google.com/noto/specimen/Noto+Sans+Symbols
    NotoSansSymbolsSemiBoldText("fonts/notoSansSymbols/static/NotoSansSymbols-SemiBold.ttf") { override fun baseFontSize() =
        TnmFont.TEXT
    },
    //https://www.1001freefonts.com/fantasy-fonts.php
    ImmortalLarge("fonts/immortal/IMMORTAL_large.ttf") { override fun baseFontSize() = TnmFont.LARGE },
    ImmortalMedium("fonts/immortal/IMMORTAL_medium.ttf") { override fun baseFontSize() = TnmFont.MEDIUM },
    ImmortalSmall("fonts/immortal/IMMORTAL_small.ttf") { override fun baseFontSize() = TnmFont.SMALL }
    ;
    abstract fun baseFontSize() : TnmFont
    open fun ftflp() = Companion.ftflp().apply { this.fontFileName = path }

    override fun load() = AssetManagerHandler.loadAssetByPath(path, ftflp())
    override fun get() = AssetManagerHandler.getAsset<BitmapFont>(path).apply { this.data.setScale(baseFontSize().fontScale) }

    companion object : IAssetStoreCompanion {
        override fun loadAll() = AssetManagerHandler.loadAssetsByPath<BitmapFont>(entries.map { it.path }, entries.map { it.ftflp() } )
        override fun getAll() = AssetManagerHandler.getAssets<BitmapFont>(entries.map { it.path })

        fun ftflp() = FreetypeFontLoader.FreeTypeFontLoaderParameter().apply {
            this.fontParameters.genMipMaps = true
            this.fontParameters.minFilter = Texture.TextureFilter.MipMapLinearLinear
            this.fontParameters.magFilter = Texture.TextureFilter.Linear
            this.fontParameters.characters += CharacterSetExtension.characters()
            this.fontParameters.size = TnmFont.baseFontSize
        }
    }
}