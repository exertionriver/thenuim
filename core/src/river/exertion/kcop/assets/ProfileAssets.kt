package river.exertion.kcop.assets

import com.badlogic.gdx.assets.AssetManager
import ktx.assets.getAsset
import ktx.assets.load

enum class ProfileAssets(val path: String) {
    ExertionRiverText("kcop/profile/er_profile.json")
}

fun AssetManager.load(asset: ProfileAssets) = load<ProfileAsset>(asset.path)
operator fun AssetManager.get(asset: ProfileAssets) = getAsset<ProfileAsset>(asset.path).also {
    if (it.status != null) println ("Profile Status: ${it.status}")
    if (it.statusDetail != null) println ("Profile Detail: ${it.statusDetail}")
}