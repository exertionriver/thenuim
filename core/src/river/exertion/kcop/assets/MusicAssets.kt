package river.exertion.kcop.assets

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Music
import ktx.assets.getAsset
import ktx.assets.load

enum class MusicAssets(val path: String) {
}
    fun AssetManager.load(asset: MusicAssets) = load<Music>(asset.path)
    operator fun AssetManager.get(asset: MusicAssets) = getAsset<Music>(asset.path)
