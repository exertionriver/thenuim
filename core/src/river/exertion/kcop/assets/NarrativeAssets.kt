package river.exertion.kcop.assets

import com.badlogic.gdx.assets.AssetManager
import ktx.assets.getAsset
import ktx.assets.load

enum class NarrativeAssets(val path: String) {
    NarrativeTest("kcop/narrative/nsb_kcop.json"),
    NarrativeNavigationTest("kcop/narrative/nnb_kcop.json"),
    NarrativeTimelineTest("kcop/narrative/ntb_kcop.json"),
    NarrativeLayoutTest("kcop/narrative/nlb_kcop.json"),
}

fun AssetManager.load(asset: NarrativeAssets) = load<NarrativeAsset>(asset.path)
operator fun AssetManager.get(asset: NarrativeAssets) = getAsset<NarrativeAsset>(asset.path).also {
    if (it.status != null) println ("Asset Status: ${it.status}")
    if (it.statusDetail != null) println ("Status Detail: ${it.statusDetail}")
}