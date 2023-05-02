package river.exertion.kcop.asset

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import ktx.assets.getAsset
import ktx.assets.load

enum class BitmapFontAssets(val path: String) {
    OpenSansRegular("fonts/openSans/OSR.fnt")
}

val bfp = BitmapFontParameter().apply {
    this.genMipMaps = true
    this.minFilter = Texture.TextureFilter.Linear
    this.magFilter = Texture.TextureFilter.Linear
}

fun AssetManager.load(asset: BitmapFontAssets) = load<BitmapFont>(asset.path, bfp)
operator fun AssetManager.get(asset: BitmapFontAssets) = getAsset<BitmapFont>(asset.path)