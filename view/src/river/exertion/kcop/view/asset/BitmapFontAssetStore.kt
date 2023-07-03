package river.exertion.kcop.view.asset

import com.badlogic.gdx.assets.loaders.BitmapFontLoader
import com.badlogic.gdx.graphics.Texture

enum class BitmapFontAssetStore(val path: String) {
    OpenSansRegular("fonts/openSans/OSR.fnt")
    ;
    companion object {
        val bfp = BitmapFontLoader.BitmapFontParameter().apply {
            this.genMipMaps = true
            this.minFilter = Texture.TextureFilter.Linear
            this.magFilter = Texture.TextureFilter.Linear
        }
    }
}