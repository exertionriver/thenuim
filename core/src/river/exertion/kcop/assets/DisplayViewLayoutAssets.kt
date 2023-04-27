package river.exertion.kcop.assets

import com.badlogic.gdx.assets.AssetManager
import ktx.assets.getAsset
import ktx.assets.load

enum class DisplayViewLayoutAssets(val path: String) {
    BasicPictureNarrative("kcop/layout/layoutBasicPictureNarrative.json"),
    GoldenRatio("kcop/layout/layoutGoldenRatio.json")
}

fun AssetManager.load(asset: DisplayViewLayoutAssets) = load<DisplayViewLayoutAsset>(asset.path)
operator fun AssetManager.get(asset: DisplayViewLayoutAssets) = getAsset<DisplayViewLayoutAsset>(asset.path)
