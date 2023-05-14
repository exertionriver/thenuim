package river.exertion.kcop.view.layout

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import river.exertion.kcop.asset.view.ColorPalette
import river.exertion.kcop.view.KcopSkin

enum class ViewType {

    DISPLAY {
        override fun viewRect(screenWidth: Float, screenHeight: Float) = Rectangle(0f, 0f, firstWidth(screenWidth), firstHeight(screenHeight))
        override fun defaultColor() = firstDefaultColor
    },
    DISPLAY_FULLSCREEN {
        override fun viewRect(screenWidth: Float, screenHeight: Float) = Rectangle(secondWidth(screenWidth) / 2, 0f, firstWidth(screenWidth), firstHeight(screenHeight))
        override fun defaultColor() = firstDefaultColor
    },
    TEXT {
        override fun viewRect(screenWidth: Float, screenHeight: Float) = Rectangle(firstWidth(screenWidth), thirdHeight(screenHeight) - 2, secondWidth(screenWidth), secondHeight(screenHeight) + 2)
        override fun defaultColor() = firstDefaultColor.comp().triad().first
    },
    LOG {
        override fun viewRect(screenWidth: Float, screenHeight: Float) = Rectangle(firstWidth(screenWidth) + fourthWidth(screenWidth), 0f, thirdWidth(screenWidth) + 1, thirdHeight(screenHeight))
        override fun defaultColor() = firstDefaultColor.triad().first
    },
    STATUS {
        override fun viewRect(screenWidth: Float, screenHeight: Float) = Rectangle(firstWidth(screenWidth), 0f, fourthWidth(screenWidth), fourthHeight(screenHeight))
        override fun defaultColor() = firstDefaultColor.triad().second
    },
    MENU {
        override fun viewRect(screenWidth: Float, screenHeight: Float) = Rectangle(firstWidth(screenWidth), fourthHeight(screenHeight), fifthWidth(screenWidth), fifthHeight(screenHeight) - 1)
        override fun defaultColor() = secondDefaultColor
    },
    INPUT {
        override fun viewRect(screenWidth: Float, screenHeight: Float) = Rectangle(firstWidth(screenWidth) + fifthWidth(screenWidth), fourthHeight(screenHeight) + seventhHeight(screenHeight), sixthWidth(screenWidth), sixthHeight(screenHeight) - 1.3f)
        override fun defaultColor() = secondDefaultColor.comp()
    },
    AI {
        override fun viewRect(screenWidth: Float, screenHeight: Float) = Rectangle(firstWidth(screenWidth) + fifthWidth(screenWidth) + seventhWidth(screenWidth), fourthHeight(screenHeight), seventhWidth(screenWidth), seventhHeight(screenHeight))
        override fun defaultColor() = secondDefaultColor.triad().first
    },
    PAUSE {
        override fun viewRect(screenWidth: Float, screenHeight: Float) = Rectangle(firstWidth(screenWidth) + fifthWidth(screenWidth), fourthHeight(screenHeight), seventhWidth(screenWidth), seventhHeight(screenHeight))
        override fun defaultColor() = secondDefaultColor.triad().second
    }
    ;

    fun viewPosition(screenWidth : Float, screenHeight : Float) : Vector2 = Vector2(viewRect(screenWidth, screenHeight).x, viewRect(screenWidth, screenHeight).y)
    abstract fun viewRect(screenWidth : Float, screenHeight : Float) : Rectangle
    abstract fun defaultColor() : ColorPalette

    val firstDefaultColor = ColorPalette.Color100
    val secondDefaultColor = ColorPalette.Color012

    companion object {
        val widths = listOf(34, 21, 13, 8, 5, 3, 2, 1)

        fun firstWidth(width : Float) = widths[1] * width / widths[0]
        fun firstHeight(height : Float) = widths[1] * height / widths[1]

        fun secondWidth(width : Float) = widths[2] * width / widths[0]
        fun secondHeight(height : Float) = widths[2] * height / widths[1]

        fun thirdWidth(width : Float) = widths[3] * width / widths[0]
        fun thirdHeight(height : Float) = widths[3] * height / widths[1]

        fun fourthWidth(width : Float) = widths[4] * width / widths[0]
        fun fourthHeight(height : Float) = widths[4] * height / widths[1]

        fun fifthWidth(width : Float) = widths[5] * width / widths[0]
        fun fifthHeight(height : Float) = widths[5] * height / widths[1]

        fun sixthWidth(width : Float) = widths[6] * width / widths[0]
        fun sixthHeight(height : Float) = widths[6] * height / widths[1]

        fun seventhWidth(width : Float) = widths[7] * width / widths[0]
        fun seventhHeight(height : Float) = widths[7] * height / widths[1]

        fun padWidth(width: Float) = seventhWidth(width) / 2
        fun padHeight(height: Float) = seventhHeight(height) / 2
    }

}