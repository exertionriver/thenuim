package river.exertion.kcop.view.asset

import com.badlogic.gdx.audio.Music
import river.exertion.kcop.asset.AssetManagerHandler
import river.exertion.kcop.asset.IAssetStore
import river.exertion.kcop.asset.IAssetStoreCompanion

enum class MusicAssetStore(val path: String) : IAssetStore {
    NavajoNights("music/navajo_clip.wav"),
    Mystery("music/mystery_clip.wav")
    ;
    override fun load() = AssetManagerHandler.loadAssetByPath<Music>(path)
    override fun get() = AssetManagerHandler.getAsset<Music>(path)

    companion object : IAssetStoreCompanion {
        override fun loadAll() = AssetManagerHandler.loadAssetsByPath<Music>(values().map { it.path })
        override fun getAll() = AssetManagerHandler.getAssets<Music>(values().map { it.path })
    }
}