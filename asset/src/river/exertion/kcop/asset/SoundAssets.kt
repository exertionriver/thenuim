package river.exertion.kcop.asset

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Music
import ktx.assets.getAsset
import ktx.assets.load

enum class SoundAssets(val path: String) {
    Sparkly("sounds/376748__zenithinfinitivestudios__fantasy_ui_button_6.wav"),
    Enter("sounds/220197__gameaudio__click-basic.wav"), //https://freesound.org/people/GameAudio/sounds/220197/
    Click("sounds/220194__gameaudio__click-heavy.wav"), //https://freesound.org/people/GameAudio/sounds/220194/
    Swoosh("sounds/220190__gameaudio__blip-pop.wav") //https://freesound.org/people/GameAudio/sounds/220190/
}
    fun AssetManager.load(asset: SoundAssets) = load<Music>(asset.path)
    operator fun AssetManager.get(asset: SoundAssets) = getAsset<Music>(asset.path)
