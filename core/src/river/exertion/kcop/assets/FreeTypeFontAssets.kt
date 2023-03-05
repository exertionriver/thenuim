package river.exertion.kcop.assets

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter
import ktx.assets.getAsset

enum class FreeTypeFontAssets(val path: String) {
    NotoSansSymbolsSemiBold("fonts/notoSansSymbols/static/NotoSansSymbols-SemiBold.ttf"), //https://fonts.google.com/noto/specimen/Noto+Sans+Symbols
    Immortal("fonts/immortal/IMMORTAL.ttf")  //https://www.1001freefonts.com/fantasy-fonts.php
}

enum class FontSize {
    TEXT {override fun fontScale() = .28f; override fun fontTag() = "text"},
    SMALL {override fun fontScale() = .41f; override fun fontTag() = "small"},
    MEDIUM {override fun fontScale() = .55f; override fun fontTag() = "medium"},
    LARGE {override fun fontScale() = .68f; override fun fontTag() = "large"}
    ;
    abstract fun fontScale() : Float
    abstract fun fontTag() : String

    companion object {
        const val baseFontSize = 48
        fun byTag(tag : String?) = values().firstOrNull { it.fontTag() == tag } ?: TEXT
    }
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

operator fun AssetManager.get(asset: FreeTypeFontAssets) = getAsset<BitmapFont>(asset.path).apply { this.data.setScale(FontSize.TEXT.fontScale()) }
