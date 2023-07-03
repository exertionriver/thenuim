package river.exertion.kcop.view.asset

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import river.exertion.kcop.asset.AssetManagerHandler
import river.exertion.kcop.asset.IAssetStore
import river.exertion.kcop.asset.IAssetStoreCompanion

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