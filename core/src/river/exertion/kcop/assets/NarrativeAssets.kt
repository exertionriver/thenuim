package river.exertion.kcop.assets

import com.badlogic.gdx.assets.AssetManager
import ktx.assets.getAsset
import ktx.assets.load
import river.exertion.kcop.narrative.NarrativeSequence

enum class NarrativeAssets(val path: String) {
    KCopTest("kcop/narrative/nsb_kcop.json")
}

fun AssetManager.load(asset: NarrativeAssets) = load<NarrativeSequence>(asset.path)
operator fun AssetManager.get(asset: NarrativeAssets) = getAsset<NarrativeSequence>(asset.path)