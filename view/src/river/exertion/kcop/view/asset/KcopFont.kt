package river.exertion.kcop.view.asset

import com.badlogic.gdx.graphics.g2d.BitmapFont
import river.exertion.kcop.asset.AssetManagerHandler

object KcopFont {

    fun large() = font(FontSize.LARGE)
    fun medium() = font(FontSize.MEDIUM)
    fun small() = font(FontSize.SMALL)
    fun text() = font(FontSize.TEXT)

    fun font(fontSize: FontSize) : BitmapFont {
        return when (fontSize) {
            FontSize.TEXT -> AssetManagerHandler.assets[FreeTypeFontAssets.NotoSansSymbolsSemiBoldText].apply { this.data.setScale(FreeTypeFontAssets.NotoSansSymbolsSemiBoldText.baseFontSize().fontScale())}
            FontSize.SMALL -> AssetManagerHandler.assets[FreeTypeFontAssets.ImmortalSmall].apply { this.data.setScale(FreeTypeFontAssets.ImmortalSmall.baseFontSize().fontScale())}
            FontSize.MEDIUM -> AssetManagerHandler.assets[FreeTypeFontAssets.ImmortalMedium].apply { this.data.setScale(FreeTypeFontAssets.ImmortalMedium.baseFontSize().fontScale())}
            FontSize.LARGE -> AssetManagerHandler.assets[FreeTypeFontAssets.ImmortalLarge].apply { this.data.setScale(FreeTypeFontAssets.ImmortalLarge.baseFontSize().fontScale())}
        }
    }
}