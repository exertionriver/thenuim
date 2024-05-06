package river.exertion.thenuim.view.asset

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import river.exertion.thenuim.asset.AssetManagerHandler
import river.exertion.thenuim.asset.IAssetStore
import river.exertion.thenuim.asset.IAssetStoreCompanion

enum class SkinAssetStore(val path: String) : IAssetStore {
    KcopUi("skin/kcop-ui.json")
    ;
    override fun load() = AssetManagerHandler.loadAssetByPath<Skin>(path)
    override fun get() = AssetManagerHandler.getAsset<Skin>(path)

    companion object : IAssetStoreCompanion {
        override fun loadAll() = AssetManagerHandler.loadAssetsByPath<Skin>(values().map { it.path })
        override fun getAll() = AssetManagerHandler.getAssets<Skin>(values().map { it.path })
    }
}