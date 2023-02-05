package river.exertion.kcop.assets

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import ktx.assets.getAsset
import ktx.assets.load

enum class TextureAssets(val path: String) {
    KoboldA("images/kobold64.png"),
    KoboldB("images/kobold64b.png"),
    KoboldC("images/kobold64c.png")
}
    fun AssetManager.load(asset: TextureAssets) = load<Texture>(asset.path)
    operator fun AssetManager.get(asset: TextureAssets) = getAsset<Texture>(asset.path)
