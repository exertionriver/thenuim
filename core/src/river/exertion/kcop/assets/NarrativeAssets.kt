package river.exertion.kcop.assets

import com.badlogic.gdx.assets.AssetManager
import ktx.assets.getAsset
import ktx.assets.load

enum class NarrativeAssets(val narrativeFilename: String) {
    NarrativeTest("nsb_kcop"),
    NarrativeNavigationTest("nnb_kcop"),
    NarrativeTimelineTest("ntb_kcop"),
    NarrativeLayoutTest("nlb_kcop")
    ;
    fun path() = NarrativeAssets.narrativeAssetPath(narrativeFilename)
    ;
    companion object {
        val narrativeAssetLocation = "kcop/narrative/"
        val narrativeAssetExtension = ".json"
        fun narrativeAssetPath(narrativeFilename : String) = narrativeAssetLocation + narrativeFilename + narrativeAssetExtension
    }
}

fun AssetManager.load(asset: NarrativeAssets) = load<NarrativeAsset>(asset.path())
operator fun AssetManager.get(asset: NarrativeAssets) = getAsset<NarrativeAsset>(asset.path()).also {
    if (it.status != null) println ("Asset Status: ${it.status}")
    if (it.statusDetail != null) println ("Status Detail: ${it.statusDetail}")
}