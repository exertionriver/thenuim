package river.exertion.kcop.system.view

import com.badlogic.gdx.math.Rectangle
import river.exertion.kcop.system.colorPalette.ColorPalette

enum class ViewType {

    DISPLAY {
        override fun viewRect(screenWidth: Float, screenHeight: Float) = Rectangle(0f, 0f, firstWidth(screenWidth), firstHeight(screenHeight))
        override fun defaultColor() = firstDefaultColor
    },
    TEXT {
        override fun viewRect(screenWidth: Float, screenHeight: Float) = Rectangle(firstWidth(screenWidth), thirdHeight(screenHeight), secondWidth(screenWidth), secondHeight(screenHeight))
        override fun defaultColor() = firstDefaultColor.comp().triad().first
    },
    LOG {
        override fun viewRect(screenWidth: Float, screenHeight: Float) = Rectangle(firstWidth(screenWidth) + fourthWidth(screenWidth), 0f, thirdWidth(screenWidth), thirdHeight(screenHeight))
        override fun defaultColor() = firstDefaultColor.triad().first
    },
    STATUS {
        override fun viewRect(screenWidth: Float, screenHeight: Float) = Rectangle(firstWidth(screenWidth), 0f, fourthWidth(screenWidth), fourthHeight(screenHeight))
        override fun defaultColor() = firstDefaultColor.triad().second
    },
    MENU {
        override fun viewRect(screenWidth: Float, screenHeight: Float) = Rectangle(firstWidth(screenWidth), fourthHeight(screenHeight), fifthWidth(screenWidth), fifthHeight(screenHeight))
        override fun defaultColor() = secondDefaultColor
    },
    INPUT {
        override fun viewRect(screenWidth: Float, screenHeight: Float) = Rectangle(firstWidth(screenWidth) + fifthWidth(screenWidth), fourthHeight(screenHeight) + seventhHeight(screenHeight), sixthWidth(screenWidth), sixthHeight(screenHeight))
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
    
    abstract fun viewRect(screenWidth : Float, screenHeight : Float) : Rectangle
    abstract fun defaultColor() : ColorPalette

    val firstDefaultColor = ColorPalette.Color402
    val secondDefaultColor = ColorPalette.Color635

    companion object {
        fun firstWidth(width : Float) = 21 * width / 34f
        fun firstHeight(height : Float) = 21 * height / 21f

        fun secondWidth(width : Float) = 13 * width / 34f
        fun secondHeight(height : Float) = 13 * height / 21f

        fun thirdWidth(width : Float) = 8 * width / 34f
        fun thirdHeight(height : Float) = 8 * height / 21f

        fun fourthWidth(width : Float) = 5 * width / 34f
        fun fourthHeight(height : Float) = 5 * height / 21f

        fun fifthWidth(width : Float) = 3 * width / 34f
        fun fifthHeight(height : Float) = 3 * height / 21f

        fun sixthWidth(width : Float) = 2 * width / 34f
        fun sixthHeight(height : Float) = 2 * height / 21f

        fun seventhWidth(width : Float) = width / 34f
        fun seventhHeight(height : Float) = height / 21f

        fun padWidth(width: Float) = seventhWidth(width) / 2
        fun padHeight(height: Float) = seventhHeight(height) / 2
    }

}