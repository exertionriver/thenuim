package river.exertion.kcop.assets

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import ktx.assets.getAsset
import ktx.assets.load

enum class SkinAssets(val path: String) {
    CleanCrispyUi("skin/clean-crispy-ui.json"),
    KcopUi("skin/kcop-ui.json")
}

fun AssetManager.load(asset: SkinAssets) = load<Skin>(asset.path)
operator fun AssetManager.get(asset: SkinAssets) = getAsset<Skin>(asset.path)
