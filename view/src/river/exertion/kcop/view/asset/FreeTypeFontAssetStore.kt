package river.exertion.kcop.view.asset

enum class FreeTypeFontAssetStore(val path: String) {
    //https://fonts.google.com/noto/specimen/Noto+Sans+Symbols
    NotoSansSymbolsSemiBoldText("fonts/notoSansSymbols/static/NotoSansSymbols-SemiBold.ttf") { override fun baseFontSize() =
        FontSize.TEXT
    },
    //https://www.1001freefonts.com/fantasy-fonts.php
    ImmortalLarge("fonts/immortal/IMMORTAL_large.ttf") { override fun baseFontSize() = FontSize.LARGE },
    ImmortalMedium("fonts/immortal/IMMORTAL_medium.ttf") { override fun baseFontSize() = FontSize.MEDIUM },
    ImmortalSmall("fonts/immortal/IMMORTAL_small.ttf") { override fun baseFontSize() = FontSize.SMALL }
    ;
    abstract fun baseFontSize() : FontSize
}