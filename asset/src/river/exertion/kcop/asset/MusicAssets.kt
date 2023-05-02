package river.exertion.kcop.asset

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Music
import ktx.assets.getAsset
import ktx.assets.load

enum class MusicAssets(val path: String) {
    NavajoNights("music/navajo_clip.wav"),
    Mystery("music/mystery_clip.wav")
}
    fun AssetManager.load(asset: MusicAssets) = load<Music>(asset.path)
    operator fun AssetManager.get(asset: MusicAssets) = getAsset<Music>(asset.path)
