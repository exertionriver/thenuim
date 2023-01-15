package river.exertion.kcop.assets

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import ktx.assets.getAsset
import ktx.assets.load

enum class FontAssets(val path: String) {
    OpenSansRegular("fonts/OSR.fnt")
}

val bfp = BitmapFontParameter().apply {
    this.genMipMaps = true
    this.minFilter = Texture.TextureFilter.Linear
    this.magFilter = Texture.TextureFilter.Linear
}

fun AssetManager.load(asset: FontAssets) = load<BitmapFont>(asset.path, bfp)
operator fun AssetManager.get(asset: FontAssets) = getAsset<BitmapFont>(asset.path).apply {
    this.data.setScale(.3f, .3f) //32 size
}