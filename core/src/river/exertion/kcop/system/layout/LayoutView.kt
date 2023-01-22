package river.exertion.kcop.system.layout

import com.badlogic.gdx.math.Rectangle

enum class LayoutView {

    DISPLAY { override fun viewRect(screenWidth: Float, screenHeight: Float) = Rectangle(0f, 0f, firstWidth(screenWidth), firstHeight(screenHeight)) },
    TEXT { override fun viewRect(screenWidth: Float, screenHeight: Float) = Rectangle(firstWidth(screenWidth), thirdHeight(screenHeight), secondWidth(screenWidth), secondHeight(screenHeight)) },
    LOG { override fun viewRect(screenWidth: Float, screenHeight: Float) = Rectangle(firstWidth(screenWidth) + fourthWidth(screenWidth), 0f, thirdWidth(screenWidth), thirdHeight(screenHeight)) },
    MENU { override fun viewRect(screenWidth: Float, screenHeight: Float) = Rectangle(firstWidth(screenWidth), 0f, fourthWidth(screenWidth), fourthHeight(screenHeight)) },
    PROMPTS { override fun viewRect(screenWidth: Float, screenHeight: Float) = Rectangle(firstWidth(screenWidth), fourthHeight(screenHeight), fifthWidth(screenWidth), fifthHeight(screenHeight)) },
    INPUTS { override fun viewRect(screenWidth: Float, screenHeight: Float) = Rectangle(firstWidth(screenWidth) + fifthWidth(screenWidth), fourthHeight(screenHeight) + seventhHeight(screenHeight), sixthWidth(screenWidth), sixthHeight(screenHeight)) },
    AI { override fun viewRect(screenWidth: Float, screenHeight: Float) = Rectangle(firstWidth(screenWidth) + fifthWidth(screenWidth) + seventhWidth(screenWidth), fourthHeight(screenHeight), seventhWidth(screenWidth), seventhHeight(screenHeight)) },
    PAUSE { override fun viewRect(screenWidth: Float, screenHeight: Float) = Rectangle(firstWidth(screenWidth) + fifthWidth(screenWidth), fourthHeight(screenHeight), seventhWidth(screenWidth), seventhHeight(screenHeight)) }
    ;
    abstract fun viewRect(screenWidth : Float, screenHeight : Float) : Rectangle

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
    }

}