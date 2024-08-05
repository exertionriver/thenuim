package river.exertion.thenuim.view.plugin

import river.exertion.thenuim.asset.AssetStatus
import river.exertion.thenuim.asset.IAsset
import river.exertion.thenuim.view.IDisplayViewLoPa

class DisplayViewPluginAsset(var packageClass : Class<IDisplayViewLoPa>? = null) : IAsset {

    override fun assetData() : Any? = packageClass?.kotlin?.objectInstance
    fun assetDataTyped() : IDisplayViewLoPa = assetData() as IDisplayViewLoPa

    override fun assetId() : String = packageClass?.name ?: ""
    override fun assetName() : String = (assetData() as IDisplayViewLoPa?)?.tag ?: ""

    override fun assetPath() : String = newAssetFilename()
    override fun assetTitle() = assetPath()

    override var assetStatus : AssetStatus? = null

    override var persisted = false

    override fun newAssetFilename(): String = DisplayViewPluginAssets.iAssetPath(super.newAssetFilename())

    override fun assetInfo() : List<String> {

        val returnList = mutableListOf<String>()

        returnList.add("path: ${assetPath()}")
        returnList.addAll(listOf(assetId()))

        return returnList.toList()
    }
}