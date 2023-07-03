package river.exertion.kcop.view

import com.badlogic.gdx.graphics.g2d.BitmapFont
import river.exertion.kcop.view.asset.FreeTypeFontAssetStore

enum class KcopFont {
    TEXT {override fun fontScale() = .28f; override fun fontTag() = "text"},
    SMALL {override fun fontScale() = .41f; override fun fontTag() = "small"},
    MEDIUM {override fun fontScale() = .55f; override fun fontTag() = "medium"},
    LARGE {override fun fontScale() = .68f; override fun fontTag() = "large"}
    ;
    abstract fun fontScale() : Float
    abstract fun fontTag() : String

    companion object {
        const val baseFontSize = 48
        fun byTag(tag : String?) = values().firstOrNull { it.fontTag() == tag } ?: TEXT

        fun large() = font(LARGE)
        fun medium() = font(MEDIUM)
        fun small() = font(SMALL)
        fun text() = font(TEXT)

        fun font(fontSize: KcopFont) : BitmapFont {
            return when (fontSize) {
                TEXT -> FreeTypeFontAssetStore.NotoSansSymbolsSemiBoldText.get()
                SMALL -> FreeTypeFontAssetStore.ImmortalSmall.get()
                MEDIUM -> FreeTypeFontAssetStore.ImmortalMedium.get()
                LARGE -> FreeTypeFontAssetStore.ImmortalLarge.get()
            }
        }
    }
}