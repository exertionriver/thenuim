package river.exertion.kcop.assets

import com.badlogic.gdx.assets.AssetManager
import ktx.assets.getAsset
import ktx.assets.load
import river.exertion.kcop.narrative.navigation.NarrativeNavigation

enum class NarrativeNavigationAssets(val path: String) {
    KCopTest("kcop/narrative/nnb_kcop.json")
}

fun AssetManager.load(asset: NarrativeNavigationAssets) = load<NarrativeNavigation>(asset.path)
operator fun AssetManager.get(asset: NarrativeNavigationAssets) = getAsset<NarrativeNavigation>(asset.path)