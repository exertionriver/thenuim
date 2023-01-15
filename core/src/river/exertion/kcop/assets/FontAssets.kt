package river.exertion.kcop.assets

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import ktx.assets.getAsset
import ktx.assets.load

enum class FontAssets(val path: String) {
    OpenSansRegular("fonts/OSR.fnt")

}
    fun AssetManager.load(asset: FontAssets) = load<BitmapFont>(asset.path)
    operator fun AssetManager.get(asset: FontAssets) = getAsset<BitmapFont>(asset.path).apply {
        this.region.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        this.data.setScale(.3f, .3f) //32 size
    }