package river.exertion.thenuim.view

import com.badlogic.gdx.graphics.g2d.BitmapFont

enum class TnmFont {
    TEXT {
        override var fontScale = .28f
        override fun fontTag() = "text"
    },
    SMALL {
        override var fontScale = .41f
        override fun fontTag() = "small"
    },
    MEDIUM {
        override var fontScale = .55f
        override fun fontTag() = "medium"
    },
    LARGE {
        override var fontScale = .68f
        override fun fontTag() = "large"
    }
    ;
    abstract var fontScale : Float
    abstract fun fontTag() : String
    open var font : BitmapFont? = null

    companion object {
        const val baseFontSize = 48
        fun byTag(tag : String?) = values().firstOrNull { it.fontTag() == tag } ?: TEXT

        fun large() = font(LARGE)
        fun medium() = font(MEDIUM)
        fun small() = font(SMALL)
        fun text() = font(TEXT)

        fun font(fontSize: TnmFont) : BitmapFont? {
            return when (fontSize) {
                TEXT -> TEXT.font
                SMALL -> SMALL.font
                MEDIUM -> MEDIUM.font
                LARGE -> LARGE.font
            }
        }
    }
}