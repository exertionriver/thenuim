package river.exertion.kcop.assets

import com.badlogic.gdx.assets.AssetManager
import ktx.assets.getAsset
import ktx.assets.load
import river.exertion.kcop.narrative.sequence.NarrativeSequence

enum class NarrativeSequenceAssets(val path: String) {
    KCopTest("kcop/narrative/nsb_kcop.json")
}

fun AssetManager.load(asset: NarrativeSequenceAssets) = load<NarrativeSequence>(asset.path)
operator fun AssetManager.get(asset: NarrativeSequenceAssets) = getAsset<NarrativeSequence>(asset.path)