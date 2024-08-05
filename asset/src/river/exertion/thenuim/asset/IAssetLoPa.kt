package river.exertion.thenuim.asset

import river.exertion.thenuim.base.ILoPa

interface IAssetLoPa : ILoPa {

    fun loadAssets()

    override fun dispose() {
        AssetManagerHandler.dispose()

        super.dispose()
    }
}