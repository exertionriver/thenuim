package river.exertion.kcop.asset.klop

import river.exertion.kcop.asset.AssetManagerHandler
import river.exertion.kcop.base.IKlop

interface IAssetKlop : IKlop {

    fun loadAssets()

    override fun dispose() {
        AssetManagerHandler.dispose()

        super.dispose()
    }
}