package river.exertion.kcop.assets

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter
import ktx.assets.getAsset
import river.exertion.kcop.assets.FreeTypeFontAssets.Companion.textScale

enum class FreeTypeFontAssets(val path: String) {
    NotoSansSymbolsSemiBold("fonts/notoSansSymbols/static/NotoSansSymbols-SemiBold.ttf"), //https://fonts.google.com/noto/specimen/Noto+Sans+Symbols
    Immortal("fonts/immortal/IMMORTAL.ttf")  //https://www.1001freefonts.com/fantasy-fonts.php
    ;
    companion object {
        const val fontSize = 48
        const val textScale = .28f
        const val smallScale = .41f
        const val mediumScale = .55f
        const val largeScale = .68f
    }
}

fun AssetManager.load(asset: FreeTypeFontAssets) = load(asset.path, BitmapFont::class.java,
    FreeTypeFontLoaderParameter().apply {
    this.fontParameters.genMipMaps = true
    this.fontParameters.minFilter = Texture.TextureFilter.Linear
    this.fontParameters.magFilter = Texture.TextureFilter.Linear
    this.fontParameters.characters = this.fontParameters.characters + "↑" + "↓"
    this.fontParameters.size = FreeTypeFontAssets.fontSize
    this.fontFileName = asset.path
})

operator fun AssetManager.get(asset: FreeTypeFontAssets) = getAsset<BitmapFont>(asset.path).apply { this.data.setScale(textScale) }
