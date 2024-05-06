package river.exertion.thenuim.asset.klop

import river.exertion.thenuim.asset.AssetManagerHandler
import river.exertion.thenuim.base.IKlop

interface IAssetKlop : IKlop {

    fun loadAssets()

    override fun dispose() {
        AssetManagerHandler.dispose()

        super.dispose()
    }
}