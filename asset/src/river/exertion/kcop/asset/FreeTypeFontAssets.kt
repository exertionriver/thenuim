package river.exertion.kcop.asset

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter
import ktx.assets.getAsset
import river.exertion.kcop.view.FontSize

enum class FreeTypeFontAssets(val path: String) {
    //https://fonts.google.com/noto/specimen/Noto+Sans+Symbols
    NotoSansSymbolsSemiBoldText("fonts/notoSansSymbols/static/NotoSansSymbols-SemiBold.ttf") { override fun baseFontSize() =
        FontSize.TEXT
    },
    //https://www.1001freefonts.com/fantasy-fonts.php
    ImmortalLarge("fonts/immortal/IMMORTAL_large.ttf") { override fun baseFontSize() = FontSize.LARGE },
    ImmortalMedium("fonts/immortal/IMMORTAL_medium.ttf") { override fun baseFontSize() = FontSize.MEDIUM },
    ImmortalSmall("fonts/immortal/IMMORTAL_small.ttf") { override fun baseFontSize() = FontSize.SMALL }
    ;
    abstract fun baseFontSize() : FontSize
}

fun AssetManager.load(asset: FreeTypeFontAssets) = load(asset.path, BitmapFont::class.java,
    FreeTypeFontLoaderParameter().apply {
    this.fontParameters.genMipMaps = true
    this.fontParameters.minFilter = Texture.TextureFilter.MipMapLinearLinear
    this.fontParameters.magFilter = Texture.TextureFilter.Linear
    this.fontParameters.characters = this.fontParameters.characters + "↑" + "↓"
    this.fontParameters.size = FontSize.baseFontSize
    this.fontFileName = asset.path
})

operator fun AssetManager.get(asset: FreeTypeFontAssets) = getAsset<BitmapFont>(asset.path).apply { this.data.setScale(
    FontSize.TEXT.fontScale()) }
