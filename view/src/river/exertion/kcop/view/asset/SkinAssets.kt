package river.exertion.kcop.view.asset

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import ktx.assets.getAsset
import ktx.assets.load

enum class SkinAssets(val path: String) {
    KcopUi("skin/kcop-ui.json")
}

fun AssetManager.load(asset: SkinAssets) = load<Skin>(asset.path)
operator fun AssetManager.get(asset: SkinAssets) = getAsset<Skin>(asset.path)
