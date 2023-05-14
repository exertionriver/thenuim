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
            FontSize.TEXT -> AssetManagerHandler.assets[FreeTypeFontAssetStore.NotoSansSymbolsSemiBoldText].apply { this.data.setScale(FreeTypeFontAssetStore.NotoSansSymbolsSemiBoldText.baseFontSize().fontScale())}
            FontSize.SMALL -> AssetManagerHandler.assets[FreeTypeFontAssetStore.ImmortalSmall].apply { this.data.setScale(FreeTypeFontAssetStore.ImmortalSmall.baseFontSize().fontScale())}
            FontSize.MEDIUM -> AssetManagerHandler.assets[FreeTypeFontAssetStore.ImmortalMedium].apply { this.data.setScale(FreeTypeFontAssetStore.ImmortalMedium.baseFontSize().fontScale())}
            FontSize.LARGE -> AssetManagerHandler.assets[FreeTypeFontAssetStore.ImmortalLarge].apply { this.data.setScale(FreeTypeFontAssetStore.ImmortalLarge.baseFontSize().fontScale())}
        }
    }
}