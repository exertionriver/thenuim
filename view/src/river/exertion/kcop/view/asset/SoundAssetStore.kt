package river.exertion.kcop.view.asset

import com.badlogic.gdx.audio.Music
import river.exertion.kcop.asset.AssetManagerHandler
import river.exertion.kcop.asset.IAssetStore
import river.exertion.kcop.asset.IAssetStoreCompanion

enum class SoundAssetStore(val path: String) : IAssetStore {
    Sparkly("sounds/376748__zenithinfinitivestudios__fantasy_ui_button_6.wav"),
    Enter("sounds/220197__gameaudio__click-basic.wav"), //https://freesound.org/people/GameAudio/sounds/220197/
    Click("sounds/220194__gameaudio__click-heavy.wav"), //https://freesound.org/people/GameAudio/sounds/220194/
    Swoosh("sounds/220190__gameaudio__blip-pop.wav") //https://freesound.org/people/GameAudio/sounds/220190/
    ;
    override fun load() = AssetManagerHandler.loadAssetByPath<Music>(path)
    override fun get() = AssetManagerHandler.getAsset<Music>(path)

    companion object : IAssetStoreCompanion {
        override fun loadAll() = AssetManagerHandler.loadAssetsByPath<Music>(values().map { it.path })
        override fun getAll() = AssetManagerHandler.getAssets<Music>(values().map { it.path })
    }
}
