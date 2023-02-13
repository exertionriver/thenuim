package river.exertion.kcop.assets

import com.badlogic.gdx.assets.AssetManager
import ktx.assets.getAsset
import ktx.assets.load
import river.exertion.kcop.narrative.structure.Narrative

enum class NarrativeAssets(val path: String) {
    NarrativeTest("kcop/narrative/nsb_kcop.json"),
    NarrativeNavigationTest("kcop/narrative/nnb_kcop.json"),
    NarrativeTimelineTest("kcop/narrative/ntb_kcop.json")
}

fun AssetManager.load(asset: NarrativeAssets) = load<Narrative>(asset.path)
operator fun AssetManager.get(asset: NarrativeAssets) = getAsset<Narrative>(asset.path)