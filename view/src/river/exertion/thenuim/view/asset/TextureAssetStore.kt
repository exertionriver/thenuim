package river.exertion.thenuim.view.asset

import com.badlogic.gdx.graphics.Texture
import river.exertion.thenuim.asset.AssetManagerHandler
import river.exertion.thenuim.asset.IAssetStore
import river.exertion.thenuim.asset.IAssetStoreCompanion

enum class TextureAssetStore(val path: String) : IAssetStore {
    KoboldA("images/kobold64.png"),
    KoboldB("images/kobold64b.png"),
    KoboldC("images/kobold64c.png"),
    BlueSphere("images/blue_sphere.png"),
    ;
    override fun load() = AssetManagerHandler.loadAssetByPath<Texture>(path)
    override fun get() = AssetManagerHandler.getAsset<Texture>(path)

    companion object : IAssetStoreCompanion {
        override fun loadAll() = AssetManagerHandler.loadAssetsByPath<Texture>(values().map { it.path })
        override fun getAll() = AssetManagerHandler.getAssets<Texture>(values().map { it.path })
    }
}
