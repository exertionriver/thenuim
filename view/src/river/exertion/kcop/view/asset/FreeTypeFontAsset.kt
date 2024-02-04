package river.exertion.kcop.view.asset

import com.badlogic.gdx.graphics.g2d.BitmapFont
import river.exertion.kcop.asset.AssetStatus
import river.exertion.kcop.asset.IAsset

class FreeTypeFontAsset(var bitmapFont : BitmapFont = BitmapFont()) : IAsset {

    override fun assetData() : Any = bitmapFont

    override fun assetId() : String = "bitmapFont"
    override fun assetName() : String = "bitmapFont"

    override fun assetPath() : String = newAssetFilename()
    override fun assetTitle() = assetPath()

    override var assetStatus : AssetStatus? = null

    override var persisted = false

    override fun newAssetFilename(): String = FreeTypeFontAssets.iAssetPath(super.newAssetFilename())

    override fun assetInfo() : List<String> {

        val returnList = mutableListOf<String>()

        returnList.add("path: ${assetPath()}")
        returnList.addAll(listOf(assetId()))

        return returnList.toList()
    }
}