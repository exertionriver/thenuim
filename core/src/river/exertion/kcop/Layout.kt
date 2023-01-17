package river.exertion.kcop

import com.badlogic.gdx.math.Rectangle

open class Layout(open var width : Float, open var height : Float) {

    fun firstWidth() = 21 * width / 34f
    fun firstHeight() = 21 * height / 21f

    fun secondWidth() = 13 * width / 34f
    fun secondHeight() = 13 * height / 21f

    fun thirdWidth() = 8 * width / 34f
    fun thirdHeight() = 8 * height / 21f

    fun fourthWidth() = 5 * width / 34f
    fun fourthHeight() = 5 * height / 21f

    fun fifthWidth() = 3 * width / 34f
    fun fifthHeight() = 3 * height / 21f

    fun sixthWidth() = 2 * width / 34f
    fun sixthHeight() = 2 * height / 21f

    fun seventhWidth() = width / 34f
    fun seventhHeight() = height / 21f

    fun displayViewRect() = Rectangle(0f, 0f, firstWidth(), firstHeight())
    fun textViewRect() = Rectangle(firstWidth(), thirdHeight(), secondWidth(), secondHeight())
    fun logViewRect() = Rectangle(firstWidth() + fourthWidth(), 0f, thirdWidth(), thirdHeight())
    fun menuViewRect() = Rectangle(firstWidth(), 0f, fourthWidth(), fourthHeight())
    fun promptsViewRect() = Rectangle(firstWidth(), fourthHeight(), fifthWidth(), fifthHeight())
    fun inputsViewRect() = Rectangle(firstWidth() + fifthWidth(), fourthHeight() + seventhHeight(), sixthWidth(), sixthHeight())
    fun aiViewRect() = Rectangle(firstWidth() + fifthWidth() + seventhWidth(), fourthHeight(), seventhWidth(), seventhHeight())
    fun pauseViewRect() = Rectangle(firstWidth() + fifthWidth(), fourthHeight(), seventhWidth(), seventhHeight())

    fun textViewRowHeight() = seventhHeight() / 2
    fun textViewFirstCol() = firstWidth() + textViewRowHeight()
    fun textViewFirstRow() = firstHeight()
    fun textViewLastRow() = thirdHeight() + 3 * textViewRowHeight()

    fun logViewRowHeight() = textViewRowHeight()
    fun logViewFirstCol() = firstWidth() + fourthWidth() + textViewRowHeight()
    fun logViewFirstRow() = thirdHeight()
    fun logViewLastRow() = 3 * logViewRowHeight()

}