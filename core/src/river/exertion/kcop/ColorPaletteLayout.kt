package river.exertion.kcop

class ColorPaletteLayout(override var width : Float, override var height : Float) : Layout(width, height) {

    fun colorColumnWidth() = fourthWidth()

    fun firstColorColumn() = fourthWidth() / 2
    fun secondColorColumn() = firstColorColumn() + colorColumnWidth()
    fun thirdColorColumn() = secondColorColumn() + colorColumnWidth()
    fun fourthColorColumn() = thirdColorColumn() + colorColumnWidth()
    fun fifthColorColumn() = fourthColorColumn() + colorColumnWidth()

    fun firstTextColumn() = firstColorColumn() + seventhWidth()
    fun secondTextColumn() = firstTextColumn() + colorColumnWidth()
    fun thirdTextColumn() = secondTextColumn() + colorColumnWidth()
    fun fourthTextColumn() = thirdTextColumn() + colorColumnWidth()
    fun fifthTextColumn() = fourthTextColumn() + colorColumnWidth()

    fun colorRowHeight() = seventhHeight()

    fun firstColorRow() = firstHeight() - colorRowHeight()
    fun firstTextRow() = firstHeight() - colorRowHeight() / 2

    fun colorSwatchHeight() = colorRowHeight() / 2
    fun colorSwatchWidth() = firstColorColumn()

}