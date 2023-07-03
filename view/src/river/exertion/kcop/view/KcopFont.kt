package river.exertion.kcop.view

import com.badlogic.gdx.graphics.g2d.BitmapFont
import river.exertion.kcop.asset.AssetManagerHandler
import river.exertion.kcop.view.asset.FontSize
import river.exertion.kcop.view.asset.FreeTypeFontAssetStore

object KcopFont {

    fun large() = font(FontSize.LARGE)
    fun medium() = font(FontSize.MEDIUM)
    fun small() = font(FontSize.SMALL)
    fun text() = font(FontSize.TEXT)

    fun font(fontSize: FontSize) : BitmapFont {
        return when (fontSize) {
            FontSize.TEXT -> AssetManagerHandler.getAsset<BitmapFont>(FreeTypeFontAssetStore.NotoSansSymbolsSemiBoldText.path).apply { this.data.setScale(FreeTypeFontAssetStore.NotoSansSymbolsSemiBoldText.baseFontSize().fontScale())}
            FontSize.SMALL -> AssetManagerHandler.getAsset<BitmapFont>(FreeTypeFontAssetStore.ImmortalSmall.path).apply { this.data.setScale(FreeTypeFontAssetStore.ImmortalSmall.baseFontSize().fontScale())}
            FontSize.MEDIUM -> AssetManagerHandler.getAsset<BitmapFont>(FreeTypeFontAssetStore.ImmortalMedium.path).apply { this.data.setScale(FreeTypeFontAssetStore.ImmortalMedium.baseFontSize().fontScale())}
            FontSize.LARGE -> AssetManagerHandler.getAsset<BitmapFont>(FreeTypeFontAssetStore.ImmortalLarge.path).apply { this.data.setScale(FreeTypeFontAssetStore.ImmortalLarge.baseFontSize().fontScale())}
        }
    }
}